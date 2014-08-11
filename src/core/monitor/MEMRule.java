package core.monitor;

import conf.CommonConf;
import conf.CommonConstants;


public class MEMRule extends Rule{

	
private static final float MEM_RULE_LOWPERCENT = CommonConf.getConf().getFloat(CommonConstants.MEM_RULE_LOWPERCENT,0.80F);;
	
	public MEMRule(){
		super();
		this.lowPercent = MEM_RULE_LOWPERCENT;
	}

}
