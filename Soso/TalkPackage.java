package Soso;

//�����ײ���

public class TalkPackage extends ServicePackage implements CallService, SendService {
	private int talkTime;// ͨ��ʱ��
	private int smsCount;// ��������

	public TalkPackage() {
		super();
	}

	public TalkPackage(double price, int talkTime, int smsCount) {
		super(price);
		this.talkTime = talkTime;
		this.smsCount = smsCount;
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

	@Override
	// ����ͨ��ʱ��
	public int call(int minCount, MobileCard card) throws Exception {
		int temp = minCount;
		for (int i = 0; i < minCount; i++) {
			if (this.talkTime - card.getRealTalkTime() >= 1) {
				// ��һ��������ײ�ʣ��ͨ��ʱ������֧��1����ͨ��
				card.setRealTalkTime(card.getRealTalkTime() + 1);
			} else if (card.getMoney() >= 0.2) {
				// �ڶ���������ײ�ͨ��ʱ�������꣬�˻�������֧��1����ͨ����ʹ���˻����֧��
				card.setRealTalkTime(card.getRealTalkTime() + 1); // ʵ��ʹ��ͨ��ʱ��1����
				// �˻��������0.2Ԫ��1���� ����ͨ����
				card.setMoney(card.getMoney() - 0.2);
				card.setConsumAmount(card.getConsumAmount() + 0.2); // �������ѽ�� +
																	// 0.2
			} else {
				temp = i; // ��¼ʵ��ͨ��������
				throw new Exception("�����Ѿ�ͨ��" + i + "���ӣ��������㣬���ֵ����ʹ�ã�");
			}
		}
		return temp;
	}

	@Override
	// ����ʵ�ʷ��Ͷ�������
	public int send(int count, MobileCard card) throws Exception {
		int temp = count;
		for (int i = 0; i < count; i++) {
			if (this.smsCount - card.getRealSMSCount() >= 1) {
				// ��һ��������ײ�ʣ��������ܹ�����һ������
				card.setRealSMSCount(card.getRealSMSCount() + 1);
			} else if (card.getMoney() >= 0.1) {
				// �ڶ���������ײ��ڶ����Ѿ����꣬ʣ�»����ܹ�����һ������
				card.setRealSMSCount(card.getRealSMSCount() + 1); // ʵ�ʶ������� 1 ��
				// �˻��������0.1Ԫ����һ�����ţ�
				card.setMoney(card.getMoney() - 0.1);
				card.setConsumAmount(card.getConsumAmount() + 0.1); // �������ѽ�� +
																	// 0.1
			} else {
				temp = i; // ��¼����������
				throw new Exception("�����Ѿ�����" + i + "�����������㣬���ֵ����ʹ�ã�");
				// System.err.println("�����Ѿ�����" + i + "�����������㣬���ֵ����ʹ�ã�");
			}
		}
		return temp;
	}

	@Override
	public void showInfo() {
		System.out.println(
				"�����ײͣ�ͨ��ʱ��Ϊ��" + this.talkTime + "����/��,��������Ϊ��" + this.smsCount + "��/��,�ʷ�Ϊ��" + super.getPrice() + "Ԫ/��");
	}

}