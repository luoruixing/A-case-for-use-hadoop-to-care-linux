package core.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import conf.CommonConf;
import conf.CommonConstants;

public abstract class ClusterStatus {
	
	private static final String HOSTNAME = "#HOSTNAME#";
	private static final String PASSWORD = "#PASSWORD#";
	protected static String commandTemplate = "sshpass -p " 
	+ PASSWORD + " ssh "+HOSTNAME;
	
	
	//sshpass -p +password ssh username@hostname + command such as df
	private static Log log=LogFactory.getLog(ClusterStatus.class);
	protected String hostname="";
	protected String toRun="";
	private Process process = null;
	private InputStream in = null;
	protected BufferedReader reader =null;
	protected String command = "";
	protected static Rule alertRule=null;
	protected long timestamp=System.currentTimeMillis();
	
	protected String type =null;
	protected float usePercent=0.0F;
	protected float userdResource=0.0F;
	protected float totalResource=0.0F;
	protected float availableResouce=0.0F;
	
	public ClusterStatus(String hostname){
		super();
		this.hostname=hostname;
	}
	
	/**
	 * parse return iuputStream
	 * @throws IOException
	 */
	public abstract void parse() throws IOException;
	
	/**
	 * run process and command
	 * @throws IOException 
	 */
	public void run() throws IOException{
		log.debug("ready to run command"+toRun);
		Runtime run=Runtime.getRuntime();
		process = run.exec(new String[]{"/bin/sh","-c",this.toRun});
		
		ExecutorService exec = null;
		try{
			exec = Executors.newFixedThreadPool(1);
			Future<InputStream> future = exec.submit(new Callable<InputStream>() {

				@Override
				public InputStream call() throws Exception {
					
					InputStream result = null;
					try{
						process.waitFor();
						result = process.getInputStream();
					}catch(Exception e){
						log.warn(e.getMessage(),e);
					}
					return result;
				}
			});
			this.in=future.get(20L,TimeUnit.SECONDS);
			if(null!=this.in){
				this.reader=new BufferedReader(new InputStreamReader(this.in));
			}
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}finally{
			if(null!=exec){
				exec.shutdown();
			}
		}
	}
	
	/**
	 * close inputstream reader
	 * @throws IOException 
	 */
	public void close() throws IOException{
		if(null!=this.reader){
			this.reader.close();
			this.reader=null;
		}
		
		if(null!=this.process){
			this.process.destroy();
			this.process=null;
		}
	}
	
	public void parseCommand(){
		this.toRun=commandTemplate + this.getCommand();
		this.toRun = this.toRun.replaceAll(PASSWORD, CommonConf.getConf().get(CommonConstants.HOST_PASSWORD));
		this.toRun = this.toRun.replaceAll(HOSTNAME, CommonConf.getConf().get(CommonConstants.REAL_USERNAME)+"@"+this.hostname);
	}
	
	public void process() throws IOException{
		this.parseCommand();
		this.run();
		this.parse();
	}
	
	
	

	public static String getCommandTemplate() {
		return commandTemplate;
	}

	public static void setCommandTemplate(String commandTemplate) {
		ClusterStatus.commandTemplate = commandTemplate;
	}

	public static Log getLog() {
		return log;
	}

	public static void setLog(Log log) {
		ClusterStatus.log = log;
	}


	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getToRun() {
		return toRun;
	}

	public void setToRun(String toRun) {
		this.toRun = toRun;
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public InputStream getIn() {
		return in;
	}

	public void setIn(InputStream in) {
		this.in = in;
	}

	public BufferedReader getReader() {
		return reader;
	}

	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public static String getHostname() {
		return HOSTNAME;
	}

	public static String getPassword() {
		return PASSWORD;
	}

	public float getUsePercent() {
		return usePercent;
	}

	public void setUsePercent(float usePercent) {
		this.usePercent = usePercent;
	}

	public float getUserdResource() {
		return userdResource;
	}

	public void setUserdResource(float userdResource) {
		this.userdResource = userdResource;
	}

	public float getTotalResource() {
		return totalResource;
	}

	public void setTotalResource(float totalResource) {
		this.totalResource = totalResource;
	}

	public float getAvailableResouce() {
		return availableResouce;
	}

	public void setAvailableResouce(float availableResouce) {
		this.availableResouce = availableResouce;
	}

	public static Rule getAlertRule() {
		return alertRule;
	}

	public static void setAlertRule(Rule alertRule) {
		ClusterStatus.alertRule = alertRule;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ClusterStatus [hostname=" + hostname + ", toRun=" + toRun
				+ ", process=" + process + ", in=" + in + ", reader=" + reader
				+ ", command=" + command + ", timestamp=" + timestamp
				+ ", type=" + type + ", usePercent=" + usePercent
				+ ", useResource=" + userdResource + ", totalResource="
				+ totalResource + ", availResource=" + availableResouce + "]";
	}
	
	
}
