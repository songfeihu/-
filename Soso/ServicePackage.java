package Soso;

// �ʷ��ײ��� ,�ǻ��롢���桢�����ײ͵ĸ���

public abstract class ServicePackage {
	private double price; // ���ʷ�

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public ServicePackage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ServicePackage(double price) {
		super();
		this.price = price;
	}

	public abstract void showInfo();

}