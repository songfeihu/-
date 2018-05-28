package Soso;

import java.util.Scanner;

//ҵ����
public class SosoMgr {

	Scanner input = new Scanner(System.in);

	// ʵ�����˵�
	public void mainMenu() {

		String number = ""; // �мǲ�Ҫ�ŵ�ѭ������ �������³�ʼ���ᱨ��ָ���쳣

		boolean flag = true; // ����do-whileѭ��

		do {

			// ��ʾһ���˵�

			showLevel1Menu();

			System.out.print("��ѡ��");

			String num = input.next();
			switch (num) {
			case "1":
				System.out.println("ִ��ʹ��(�û���¼)����");
				System.out.print("�����������ֻ��ţ�");
				number = input.next();
				System.out.print("�������������룺");
				String passWord = input.next();
				// ͨ���ֻ����жϸ��û��Ƿ����
				CardUtil.isExistCard(number);
				// ͨ���ֻ��ź������жϸ��û��Ƿ����
				if (CardUtil.isExistCard(number, passWord)) {
					cardMenu(number);
				}
				flag = true;
				break;

			case "2":
				System.out.println("ִ��ʹ��(�û�ע��)����");
				registCard();
				flag = true;
				break;

			case "3":
				System.out.println("ִ��ʹ��(ʹ����)����");
				System.out.println("�������ֻ����ţ�");
				String soso_Number = input.next();
				if (CardUtil.isExistCard(soso_Number)) { // ����û�������ִ��Soso����
					CardUtil.userSoso(soso_Number);
				}

				flag = true;
				break;

			case "4":
				System.out.println("ִ��ʹ��(���ѳ�ֵ)����");
				System.out.println("�������ֵ���ţ�");
				String refill_Number = input.next();
				if (CardUtil.isExistCard(refill_Number)) { // ����û�������ִ�г�ֵ����
					CardUtil.chargeMoney(refill_Number);
				}

				flag = true;
				break;

			case "5":
				CardUtil.showDescription();
				flag = true;
				break;

			case "6":
				System.out.println("ִ��ʹ��(�˳�ϵͳ)����");
				System.out.println("ллʹ��");
				flag = false; // �˳�do-whileѭ��
				break;
			default:
				System.out.println("�������,��������");
				flag = true;
				break;
			}
		} while (flag);
	}

	// ʵ�ֶ����˵�

	public void cardMenu(String number) {

		boolean flag = true; // ����do-whileѭ��
		while (flag) {
			// ��ʾ�����˵�
			showLevel2Menu();
			System.out.println("��ѡ������1~5ѡ���ܣ�������������һ������");
			String num = input.next();
			switch (num) {
			case "1":
				System.out.println("ִ��ʹ��(�����˵���ѯ)����");
				CardUtil.showAmountDetail(number);
				flag = true;
				break;
			case "2":
				System.out.println("ִ��ʹ��(�ײ�������ѯ)����");
				CardUtil.showRemainDetail(number);
				flag = true;
				break;
			case "3":
				System.out.println("ִ��ʹ��(��ӡ��������)����");
				CardUtil.printAmountDetail(number);
				// �����õ���System.out.println(CardUtil.consumInfos.keySet());
				flag = true;
				break;
			case "4":
				System.out.println("ִ��ʹ��(�ײͱ��)����");
				CardUtil.changingPack(number);
				flag = true;
				break;
			case "5":
				System.out.println("ִ��ʹ��(��������)����");
				CardUtil.delCard(number);
				flag = true;
				break;
			default:
				flag = false;
				break;
			}
			if (flag) {
				System.out.print("����0������һ��,����������������ҳ��");
				String strNum = input.next();
				if (strNum.equals("0")) {
					continue;
				} else {
					flag = false;
				}
			}
		}
	}

	// ��ʾһ���˵�
	public static void showLevel1Menu() {
		System.out.println("*****************��ӭʹ�����ƶ�ҵ�����***************");
		System.out.println("1.�û���¼   2.�û�ע��  3.ʹ����  4.���ѳ�ֵ  5.�ʷ�˵��  6.�˳�ϵͳ");
	}

	// ��ʾ�����˵�
	public void showLevel2Menu() {
		System.out.println("***********���ƶ��û��˵�***********");
		System.out.println("1.�����˵���ѯ");
		System.out.println("2.�ײ�������ѯ");
		System.out.println("3.��ӡ�����굥");
		System.out.println("4.�ײͱ��");
		System.out.println("5��������");
	}

	// �û�ע������
	public void registCard() {
		// �����ײ�
		MobileCard mobileCard = new MobileCard();

		System.out.println("************��ѡ��Ŀ���************");

		// ͨ������ѭ�������������

		String[] cardNumbers = CardUtil.getNewNumbers(9);

		for (int i = 0; i < cardNumbers.length; i++) {

			System.out.print((i + 1) + "." + cardNumbers[i] + "\t");

			if (2 == i || 5 == i || 8 == i) {

				System.out.println();
			}
		}
		System.out.print("��ѡ�񿨺ţ�");
		while (true) { // ��ѭ�����ڲ��Ĺ��ܣ���֤����ʹ��input.nextInt������£����������ĸҲ���ᱨ�쳣

			Scanner input = new Scanner(System.in); // ����䲻�ܷŵ�whileѭ�����ⲿ������������ѭ��

			if (input.hasNextInt() == true) { // input.hasNextInt() == true
				// �ж������Ƿ�Ϊint��
				int num = input.nextInt();

				if (0 < num && num < 10) {

					mobileCard.setCardNumber(cardNumbers[num - 1]);
					break;
				} else {
					System.out.print("������������루1~9��������:");
					continue;
				}
			} else {
				System.out.print("������������루1~9��������:");
				continue;
			}
		}
		System.out.println("1.�����ײ�  2.�����ײ�  3.�����ײ� �� ��ѡ���ײͣ�������ţ�:");

		boolean bol = true;

		while (bol) {
			String packageNumStr = input.next();
			switch (packageNumStr) {
			case "1":
				mobileCard.setSerPackage(Common.talkPackage);
				bol = false;
				break;
			case "2":
				mobileCard.setSerPackage(Common.netPackage);
				bol = false;
				break;
			case "3":
				mobileCard.setSerPackage(Common.superPackage);
				bol = false;
				break;
			default:
				System.out.println("�������������ѡ��");
				bol = true;
				break;
			}
		}

		System.out.println("������������");
		String userName = input.next();
		mobileCard.setUserName(userName);
		System.out.println("���������룺");
		String passWord = input.next();
		mobileCard.setPassWord(passWord);
		System.out.println("������Ԥ�滰�ѣ�");
		double money = 0.0;
		boolean flag = false;// ����ѭ���Լ�����if���
		do {
			if (flag == true) {
				System.out.println("��Ԥ��Ļ��ѽ�����֧�����¹̶��ײ��ʷ�(" + mobileCard.getSerPackage().getPrice() + "Ԫ)�������³�ֵ:");
			}

			while (true) {

				Scanner input = new Scanner(System.in);
				if (input.hasNextDouble() == true) {
					money = input.nextDouble();
					break;
				} else {
					System.out.println("�����������������");
				}
			}
			/*
			 * flag = (money < 58 && mobileCard.getSerPackage() ==
			 * Common.talkPackage) || (money < 68 && mobileCard.getSerPackage()
			 * == Common.netPackage) || (money < 78 &&
			 * mobileCard.getSerPackage() == Common.superPackage);
			 */
		} while (flag);

		mobileCard.setMoney(money);

		CardUtil.cards.put(mobileCard.getCardNumber(), mobileCard);
		// �ж� �ײ�
		if (mobileCard.getSerPackage() == Common.talkPackage) {
			System.out.println("ע��ɹ������ţ�" + mobileCard.getCardNumber() + ",�û�����" + mobileCard.getUserName() + ",��ǰ���Ϊ��"
					+ (mobileCard.getMoney() - Common.talkPackage.getPrice()) + "Ԫ");
		} else if (mobileCard.getSerPackage() == Common.netPackage) {
			System.out.println("ע��ɹ������ţ�" + mobileCard.getCardNumber() + ",�û�����" + mobileCard.getUserName() + ",��ǰ���Ϊ��"
					+ (mobileCard.getMoney() - Common.netPackage.getPrice()) + "Ԫ");
		} else if (mobileCard.getSerPackage() == Common.superPackage) {
			System.out.println("ע��ɹ������ţ�" + mobileCard.getCardNumber() + ",�û�����" + mobileCard.getUserName() + ",��ǰ���Ϊ��"
					+ (mobileCard.getMoney() - Common.superPackage.getPrice()) + "Ԫ");
		}
		mobileCard.getSerPackage().showInfo();

		System.out.println("\n");
	}

	public static void main(String[] args) {
		// ��ʼ��һ����Ϣ
		MobileCard mobileCard = new MobileCard("18810152602", "����", "998", Common.superPackage, 0, 100, 0, 0, 0);
		CardUtil.addCard(mobileCard);
		SosoMgr sosoMgr = new SosoMgr();
		sosoMgr.mainMenu();
	}

}