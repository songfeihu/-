package Soso;

// 资费套餐类 ,是话唠、网虫、超人套餐的父类

public abstract class ServicePackage {
	private double price; // 月资费

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