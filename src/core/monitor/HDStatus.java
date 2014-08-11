package core.monitor;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HDStatus extends ClusterStatus{

	private static Log LOG = LogFactory.getLog(HDStatus.class);
    public static final String COMMAND = " 'df '";

	
    static {
    	alertRule = new HDRule();
    }
    
	public HDStatus(String hostname) {
		super(hostname);
		this.command = COMMAND;
		this.type = ErrorInfoType.HD;
	}

	@Override
	public void parse() throws IOException {
		if( null != this.reader){
			try{
			String line = null;
			while(null != (line = reader.readLine())){
				String[] array = line.split("[\\s]+");
				if(array[0].startsWith("/dev/")){
					LOG.debug(line);
					this.totalResource += Float.parseFloat(array[1]);
					this.userdResource += Float.parseFloat(array[2]);
					this.availableResouce += Float.parseFloat(array[3]);
				}
			}
			if(this.totalResource >0){
				this.usePercent = this.userdResource / this.totalResource;
				
			}
			}catch(Exception e){
				LOG.debug(e.getMessage(),e);
			}finally{
				close();
			}
		}
	}
	
    public static void main(String[] args){
    	
    	ClusterStatus status = new HDStatus("localhost");
    	try {
			status.process();
			System.out.println(status.getTotalResource());
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }

}

