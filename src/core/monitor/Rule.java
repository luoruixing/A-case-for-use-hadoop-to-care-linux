package core.monitor;

import conf.CommonConf;
import conf.CommonConstants;

public abstract class Rule {
	
	protected float lowPercent=0.80F;
	protected long remainMillSeconds = 0;//check interval
	
	public Rule(){
		super();
		this.lowPercent = 0.8F;
		this.remainMillSeconds=CommonConf.getConf().getLong(CommonConstants.MONITOR_CHECK_INTERVAL,1000L);
	}

	public float getLowPercent() {
		return lowPercent;
	}

	public void setLowPercent(float lowPercent) {
		this.lowPercent = lowPercent;
	}

	public long getRemainMillSeconds() {
		return remainMillSeconds;
	}

	public void setRemainMillSeconds(long remainMillSeconds) {
		this.remainMillSeconds = remainMillSeconds;
	}

	@Override
	public String toString() {
		return "Rule [lowPercent=" + lowPercent + ", remainMillSeconds="
				+ remainMillSeconds + "]";
	}

	
	
}
