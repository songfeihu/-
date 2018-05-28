package Soso;

public class NetPackage extends ServicePackage implements NetService {
	private int flow; // 上网流量

	public NetPackage() {
		super();
	}

	public NetPackage(double price, int flow) {
		super(price);
		this.flow = flow;
	}

	public int getFlow() {
		return flow;
	}

	public void setFlow(int flow) {
		this.flow = flow;
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
				// System.err.println("流量已经使用" + i + "MB，您的余额不足，请充值后再使用！");
			}
		}
		return temp;
	}

	@Override
	public void showInfo() {
		System.out.println("网虫套餐：上网流量为：" + (this.flow * 1.0 / 1024) + "GB/月," + "资费为：" + super.getPrice() + "元/月");
	}

}