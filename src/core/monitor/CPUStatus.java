package core.monitor;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CPUStatus extends ClusterStatus{

	
	private static Log LOG = LogFactory.getLog(CPUStatus.class);
    public static final String COMMAND = " 'top -bcn 1'";

	
    static {
    	
    	alertRule = new CPURule();
    }
    
	public CPUStatus(String hostname) {
		super(hostname);
		this.command = COMMAND;
		this.type = ErrorInfoType.CPU;	
		}

	@Override
	public void parse() throws IOException {
		if( null != this.reader){
			try{
			String line = null;
			int counter = 0;
			while(null != (line = reader.readLine())){
				counter++;
				if(counter == 3){
				String[] array = line.split("[\\s]+");
			    this.totalResource += Float.parseFloat("1");
					this.userdResource +=(Float.parseFloat(array[1].split("%")[0]))*0.01F+Float.parseFloat(array[2].split("%")[0])*0.01F;
					this.availableResouce += this.totalResource -this.userdResource;
					this.usePercent = this.userdResource / this.totalResource;
					break;
				}
			}
			}catch(Exception e){
				LOG.debug(e.getMessage(),e);
			}finally{
				close();
			}
		}
	}

    public static void main(String[] args){
    	
    	ClusterStatus status = new CPUStatus("localhost");
    	try {
			status.process();
			System.out.println(status.getUsePercent());
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }

}
