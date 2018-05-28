package Soso;

public class NetPackage extends ServicePackage implements NetService {
	private int flow; // ��������

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
	// ����ʹ��������
	public int netPlay(int flow, MobileCard card) throws Exception {
		int temp = flow;
		for (int i = 0; i < flow; i++) {
			if (this.flow - card.getRealFlow() >= 1) {
				// ��һ�����������ʣ�๻ 1 MB
				card.setRealFlow(card.getRealFlow() + 1);
			} else if (card.getMoney() >= 0.1) {
				// �ڶ���������ײ��������Ѿ����꣬ʣ�»��ѹ�֧�� 1 MB������
				card.setRealFlow(card.getRealFlow() + 1); // ʵ�ʶ������� 1 ��
				// �˻��������0.1Ԫ����1MB������
				card.setMoney(card.getMoney() - 0.1);
				card.setConsumAmount(card.getConsumAmount() + 0.1); // �������ѽ�� +
																	// 0.1
			} else {
				temp = i; // ��¼ʹ����������MB
				throw new Exception("�����Ѿ�ʹ��" + i + "MB���������㣬���ֵ����ʹ�ã�");
				// System.err.println("�����Ѿ�ʹ��" + i + "MB���������㣬���ֵ����ʹ�ã�");
			}
		}
		return temp;
	}

	@Override
	public void showInfo() {
		System.out.println("�����ײͣ���������Ϊ��" + (this.flow * 1.0 / 1024) + "GB/��," + "�ʷ�Ϊ��" + super.getPrice() + "Ԫ/��");
	}

}