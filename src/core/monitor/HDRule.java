package core.monitor;

import conf.CommonConf;
import conf.CommonConstants;

public class HDRule extends Rule {
	
	private static final float HD_RULE_LOWPERCENT = CommonConf.getConf().getFloat(CommonConstants.HD_RULE_LOWPERCENT, 0.8F);
	
	public HDRule(){
		super();
		this.lowPercent=HD_RULE_LOWPERCENT;
	}
	
}
