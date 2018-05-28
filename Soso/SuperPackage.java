package Soso;

// 超人套餐类

public class SuperPackage extends ServicePackage implements SendService, NetService, CallService {
	private int talkTime; // 通话时长
	private int smsCount; // 短信条数
	private int flow; // 上网流量
	private int price = 78;//价格

	public SuperPackage() {
		super();
	}

	public SuperPackage(double price, int talkTime, int smsCount, int flow) {
		super(price);
		this.talkTime = talkTime;
		this.smsCount = smsCount;
		this.flow = flow;
	}

	public int getTalkTime() {
		return talkTime;
	}

	public void setTalkTime(int talkTime) {
		this.talkTime = talkTime;
	}

	public int getSmsCount() {
		return smsCount;
	}

	public void setSmsCount(int smsCount) {
		this.smsCount = smsCount;
	}

	public int getFlow() {
		return flow;
	}

	public void setFlow(int flow) {
		this.flow = flow;
	}

	@Override
	// 返回通话时长
	public int call(int minCount, MobileCard card) throws Exception {
		int temp = minCount;
		for (int i = 0; i < minCount; i++) {
			if (this.talkTime - card.getRealTalkTime() >= 1) {
				// 第一种情况：套餐剩余通话时长可以支持1分钟通话
				card.setRealTalkTime(card.getRealTalkTime() + 1);
			} else if (card.getMoney() >= 0.2) {
				// 第二种情况：套餐通话时长已用完，账户余额可以支付1分钟通话，使用账户余额支付
				card.setRealTalkTime(card.getRealTalkTime() + 1); // 实际使用通话时长1分钟
				// 账户余额消费0.2元（1分钟 额外通话）
				card.setMoney(card.getMoney() - 0.2);
				card.setConsumAmount(card.getConsumAmount() + 0.2); // 当月消费金额 +
																	// 0.2
			} else {
				temp = i; // 记录实际通话分钟数
				throw new Exception("本次已经通话" + i + "分钟，您的余额不足，请充值后再使用！");
				// System.err.println("本次已经通话" + i + "分钟，您的余额不足，请充值后再使用！");
			}
		}
		return temp;
	}

	@Override
	// 返回使用流量数
	public int netPlay(int flow, MobileCard card) throws Exception {
		int temp = flow;
		for (int i = 0; i < flow; i++) {
			if (this.flow - card.getRealFlow() >= 1) {
				// 第一种情况：流量剩余够 1 MB
				card.setRealFlow(card.getRealFlow() + 1);
			} else if (card.getMoney() >= 0.1) {
				// 第二种情况：套餐内流量已经用完，剩下话费够支付 1 MB的流量
				card.setRealFlow(card.getRealFlow() + 1); // 实际短信数加 1 条
				// 账户余额消费0.1元，（1MB流量）
				card.setMoney(card.getMoney() - 0.1);
				card.setConsumAmount(card.getConsumAmount() + 0.1); // 当月消费金额 +
																	// 0.1
			} else {
				temp = i; // 记录使用流量多少MB
				throw new Exception("流量已经使用" + i + "MB，您的余额不足，请充值后再使用！");
			}
		}
		return temp;
	}

	@Override
	// 返回实际发送短信条数
	public int send(int count, MobileCard card) throws Exception {
		int temp = count;
		for (int i = 0; i < count; i++) {
			if (this.smsCount - card.getRealSMSCount() >= 1) {
				// 第一种情况：套餐剩余短信数能够发送一个短信
				card.setRealSMSCount(card.getRealSMSCount() + 1);
			} else if (card.getMoney() >= 0.1) {
				// 第二种情况：套餐内短信已经用完，剩下话费能够允许发一条短信
				card.setRealSMSCount(card.getRealSMSCount() + 1); // 实际短信数加 1 条
				// 账户余额消费0.1元，（一条短信）
				card.setMoney(card.getMoney() - 0.1);
				card.setConsumAmount(card.getConsumAmount() + 0.1); // 当月消费金额 +
																	// 0.1
			} else {
				temp = i; // 记录发短信条数
				throw new Exception("短信已经发送" + i + "条，您的余额不足，请充值后再使用！");
			}
		}
		return temp;
	}

	@Override
	public void showInfo() {
		System.out.println("超人套餐：通话时长为：" + this.talkTime + "分钟/月," + "短信条数为：" + this.smsCount + "条/每月," + "上网流量为："
				+ (this.flow * 1.0 / 1024) + "GB/月," + "资费为:" + super.getPrice() + "元/月");
	}
}