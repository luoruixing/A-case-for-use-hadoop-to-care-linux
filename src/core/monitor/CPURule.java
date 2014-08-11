package core.monitor;

import conf.CommonConf;
import conf.CommonConstants;


public class CPURule extends Rule{
private static final float CPU_RULE_LOWPERCENT = CommonConf.getConf().getFloat(CommonConstants.CPU_RULE_LOWPERCENT,0.80F);;
	
	public CPURule(){
		super();
		this.lowPercent = CPU_RULE_LOWPERCENT;
	}

}
