package etl.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.script.CompiledScript;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
//log4j2
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import etl.engine.ProcessMode;
import etl.engine.ETLCmd;
import etl.util.DBType;
import etl.util.DBUtil;
import etl.util.ScriptEngineUtil;
import scala.Tuple2;

public class LoadDataCmd extends SchemaETLCmd{
	private static final long serialVersionUID = 1L;
	private static final String NO_TABLE_CONFIGURED = "<no-table-configured>";
	private static final String[] EMPTY_STRING_ARRAY = new String[0];
	public static final Logger logger = LogManager.getLogger(LoadDataCmd.class);
	//cfgkey
	public static final String cfgkey_webhdfs="hdfs.webhdfs.root";
	public static final String cfgkey_csvfile = "csv.file";
	public static final String cfgkey_load_sql = "load.sql";
	public static final String cfgkey_table_names="table.names";
	public static final String cfgkey_csv_suffix ="csv.suffix";
	//system variables
	public static final String VAR_ROOT_WEB_HDFS="rootWebHdfs";
	public static final String VAR_USERNAME="userName";
	public static final String VAR_CSV_FILE="csvFileName";
	public static final String VAR_TABLE_NAME="tableName";
	
	private String webhdfsRoot;
	private String userName;
	private String csvFile;
	private String loadSql;
	private String[] tableNames;
	
	private transient CompiledScript csCsvFile;
	private transient CompiledScript csLoadSql;
	private transient List<String> copysqls;
	
	public LoadDataCmd(){
		super();
	}
	
	public LoadDataCmd(String wfName, String wfid, String staticCfg, String defaultFs, String[] otherArgs){
		init(wfName, wfid, staticCfg, null, defaultFs, otherArgs, ProcessMode.Single);
	}
	
	public LoadDataCmd(String wfName, String wfid, String staticCfg, String defaultFs, String[] otherArgs, ProcessMode pm){
		init(wfName, wfid, staticCfg, null, defaultFs, otherArgs, pm);
	}
	
	public LoadDataCmd(String wfName, String wfid, String staticCfg, String prefix, String defaultFs, String[] otherArgs){
		init(wfName, wfid, staticCfg, prefix, defaultFs, otherArgs, ProcessMode.Single);
	}
	
	@Override
	public void init(String wfName, String wfid, String staticCfg, String prefix, String defaultFs, String[] otherArgs, ProcessMode pm){
		super.init(wfName, wfid, staticCfg, prefix, defaultFs, otherArgs, pm);
		
		this.csvFile = super.getCfgString(cfgkey_csvfile, null);
		this.webhdfsRoot = super.getCfgString(cfgkey_webhdfs, null);
		this.userName = super.getCfgString(DBUtil.key_db_user, null);
		this.loadSql = super.getCfgString(cfgkey_load_sql, null);
		logger.info(String.format("load sql:%s", loadSql));
		this.tableNames = super.getCfgStringArray(cfgkey_table_names);
		//
		this.getSystemVariables().put(VAR_ROOT_WEB_HDFS, this.webhdfsRoot);
		this.getSystemVariables().put(VAR_USERNAME, this.userName);
		if (this.csvFile!=null){
			csCsvFile = ScriptEngineUtil.compileScript(csvFile);
		}else{
			logger.warn(String.format("csvFile is not specified."));
		}
		if (this.loadSql!=null){
			csLoadSql = ScriptEngineUtil.compileScript(loadSql);
		}else{
			logger.warn(String.format("loadSql is not specified."));
		}
		copysqls = new ArrayList<String>();
	}
	
	private List<String> prepareTableCopySQLs(String tableName, String[] files) {
		List<String> newCopysqls = new ArrayList<String>();
		try{
			String sql = null;
			if (logicSchema!=null && (csLoadSql==null||loadSql.contains(VAR_TABLE_NAME))){
				this.getSystemVariables().put(VAR_TABLE_NAME, tableName);
				
				for (String csvFileName: files) {
					this.getSystemVariables().put(VAR_CSV_FILE, csvFileName);
					
					if (csLoadSql!=null){
						sql = ScriptEngineUtil.eval(csLoadSql, this.getSystemVariables());
					}else{
						sql = DBUtil.genCopyHdfsSql(null, logicSchema.getAttrNames(tableName), tableName, 
								dbPrefix, this.webhdfsRoot, csvFileName, this.userName, this.getDbtype());
					}
					newCopysqls.add(sql);

					logger.info(String.format("sql:%s", sql));
				}
			}else if (csLoadSql!=null){//just evaluate the loadSql
				for (String csvFileName: files) {
					this.getSystemVariables().put(VAR_CSV_FILE, csvFileName);
					sql = ScriptEngineUtil.eval(csLoadSql, this.getSystemVariables());
					newCopysqls.add(sql);
					
					logger.info(String.format("sql:%s", sql));
				}
			}
		}catch(Exception e){
			logger.error("", e);
		}
		return newCopysqls;
	}
	
	@Override
	public List<String> sgProcess() {
		if (this.getFs()==null) init();
		List<String> logInfo = new ArrayList<String>();
		try{
			String csvFileName = null;
			String[] files;
			if (logicSchema!=null && (csLoadSql==null||loadSql.contains(VAR_TABLE_NAME))){
				List<String> tryTables = new ArrayList<String>();
				if (tableNames==null || tableNames.length==0){//default sql, match all the files against the tables
					tryTables.addAll(logicSchema.getAttrNameMap().keySet());
				}else{
					tryTables.addAll(Arrays.asList(tableNames));
				}
			
				for (String tableName:tryTables){
					this.getSystemVariables().put(VAR_TABLE_NAME, tableName);
					csvFileName = ScriptEngineUtil.eval(this.csCsvFile, this.getSystemVariables());
					files = new String[] {csvFileName};
					copysqls.addAll(
						prepareTableCopySQLs(tableName, files)
					);
				}
			}else{//just evaluate the loadSql
				csvFileName = ScriptEngineUtil.eval(this.csCsvFile, this.getSystemVariables());
				files = new String[] {csvFileName};
				copysqls.addAll(
					prepareTableCopySQLs(null, files)
				);
			}
		}catch(Exception e){
			logger.error("", e);
		}
		
		if (super.getDbtype()!=DBType.NONE){
			int rowsAdded = DBUtil.executeSqls(copysqls, super.getPc());
			logInfo.add(rowsAdded+"");
		}
		
		return  logInfo;
	}
	
	public Map<String, Object> mapProcess(long offset, String row,
			Mapper<LongWritable, Text, Text, Text>.Context context) throws Exception {
		Map<String, Object> ret = new HashMap<String, Object>();
		List<Tuple2<String, String>> vl = new ArrayList<Tuple2<String, String>>();
		if (row.startsWith("hdfs://")) {
			/* Locate from the root path */
			row = row.substring(7);
			row = row.substring(row.indexOf("/"));
		}
		if (logicSchema!=null && (csLoadSql==null||loadSql.contains(VAR_TABLE_NAME))){
			/* File to table mapping */
			String tableName = this.getTableName(row);
			vl.add(new Tuple2<String, String>(tableName, row));
		}else{
			vl.add(new Tuple2<String, String>(NO_TABLE_CONFIGURED, row));
		}
		ret.put(RESULT_KEY_OUTPUT_TUPLE2, vl);
		return ret;
	}
	
	public List<String[]> reduceProcess(Text key, Iterable<Text> values,
			Reducer<Text, Text, Text, Text>.Context context, MultipleOutputs<Text, Text> mos) throws Exception {
		List<String[]> ret = new ArrayList<String[]>();
		if (NO_TABLE_CONFIGURED.equals(key.toString()))
			copysqls.addAll(
				prepareTableCopySQLs(null, toFiles(values))
			);
		else
			copysqls.addAll(
				prepareTableCopySQLs(key.toString(), toFiles(values))
			);
		if (super.getDbtype()!=DBType.NONE){
			int rowsAdded = DBUtil.executeSqls(copysqls, super.getPc());
			ret.add(new String[]{Integer.toString(rowsAdded), null, ETLCmd.SINGLE_TABLE});
		}
		return ret;
	}

	private String[] toFiles(Iterable<Text> values) {
		List<String> files = new ArrayList<String>();
		Iterator<Text> it = values.iterator();
		while (it.hasNext()) {
			String v = it.next().toString();
			files.add(v);
		}
		return files.toArray(EMPTY_STRING_ARRAY);
	}

	public boolean hasReduce() {
		return true;
	}

	public List<String> getCopysqls() {
		return copysqls;
	}

	public void setCopysqls(List<String> copysqls) {
		this.copysqls = copysqls;
	}

}