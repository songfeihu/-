package Soso;

import java.util.Scanner;

//业务类
public class SosoMgr {

	Scanner input = new Scanner(System.in);

	// 实现主菜单
	public void mainMenu() {

		String number = ""; // 切记不要放到循环里面 ，会重新初始化会报空指针异常

		boolean flag = true; // 控制do-while循环

		do {

			// 显示一级菜单

			showLevel1Menu();

			System.out.print("请选择：");

			String num = input.next();
			switch (num) {
			case "1":
				System.out.println("执行使用(用户登录)功能");
				System.out.print("请输入您的手机号：");
				number = input.next();
				System.out.print("请输入您的密码：");
				String passWord = input.next();
				// 通过手机号判断该用户是否存在
				CardUtil.isExistCard(number);
				// 通过手机号和密码判断该用户是否存在
				if (CardUtil.isExistCard(number, passWord)) {
					cardMenu(number);
				}
				flag = true;
				break;

			case "2":
				System.out.println("执行使用(用户注册)功能");
				registCard();
				flag = true;
				break;

			case "3":
				System.out.println("执行使用(使用嗖嗖)功能");
				System.out.println("请输入手机卡号：");
				String soso_Number = input.next();
				if (CardUtil.isExistCard(soso_Number)) { // 如果用户存在则执行Soso功能
					CardUtil.userSoso(soso_Number);
				}

				flag = true;
				break;

			case "4":
				System.out.println("执行使用(话费充值)功能");
				System.out.println("请输入充值卡号：");
				String refill_Number = input.next();
				if (CardUtil.isExistCard(refill_Number)) { // 如果用户存在则执行充值功能
					CardUtil.chargeMoney(refill_Number);
				}

				flag = true;
				break;

			case "5":
				CardUtil.showDescription();
				flag = true;
				break;

			case "6":
				System.out.println("执行使用(退出系统)功能");
				System.out.println("谢谢使用");
				flag = false; // 退出do-while循环
				break;
			default:
				System.out.println("输入错误,请重来！");
				flag = true;
				break;
			}
		} while (flag);
	}

	// 实现二级菜单

	public void cardMenu(String number) {

		boolean flag = true; // 控制do-while循环
		while (flag) {
			// 显示二级菜单
			showLevel2Menu();
			System.out.println("请选择（输入1~5选择功能，其它键返回上一级）：");
			String num = input.next();
			switch (num) {
			case "1":
				System.out.println("执行使用(本月账单查询)功能");
				CardUtil.showAmountDetail(number);
				flag = true;
				break;
			case "2":
				System.out.println("执行使用(套餐余量查询)功能");
				CardUtil.showRemainDetail(number);
				flag = true;
				break;
			case "3":
				System.out.println("执行使用(打印消费详情)功能");
				CardUtil.printAmountDetail(number);
				// 调试用到的System.out.println(CardUtil.consumInfos.keySet());
				flag = true;
				break;
			case "4":
				System.out.println("执行使用(套餐变更)功能");
				CardUtil.changingPack(number);
				flag = true;
				break;
			case "5":
				System.out.println("执行使用(办理退网)功能");
				CardUtil.delCard(number);
				flag = true;
				break;
			default:
				flag = false;
				break;
			}
			if (flag) {
				System.out.print("输入0返回上一层,输入其他键返回首页：");
				String strNum = input.next();
				if (strNum.equals("0")) {
					continue;
				} else {
					flag = false;
				}
			}
		}
	}

	// 显示一级菜单
	public static void showLevel1Menu() {
		System.out.println("*****************欢迎使用嗖嗖移动业务大厅***************");
		System.out.println("1.用户登录   2.用户注册  3.使用嗖嗖  4.话费充值  5.资费说明  6.退出系统");
	}

	// 显示二级菜单
	public void showLevel2Menu() {
		System.out.println("***********嗖嗖移动用户菜单***********");
		System.out.println("1.本月账单查询");
		System.out.println("2.套餐余量查询");
		System.out.println("3.打印消费详单");
		System.out.println("4.套餐变更");
		System.out.println("5办理退网");
	}

	// 用户注册流程
	public void registCard() {
		// 创建套餐
		MobileCard mobileCard = new MobileCard();

		System.out.println("************可选择的卡号************");

		// 通过超级循环遍历输出卡号

		String[] cardNumbers = CardUtil.getNewNumbers(9);

		for (int i = 0; i < cardNumbers.length; i++) {

			System.out.print((i + 1) + "." + cardNumbers[i] + "\t");

			if (2 == i || 5 == i || 8 == i) {

				System.out.println();
			}
		}
		System.out.print("请选择卡号：");
		while (true) { // 此循环体内部的功能，保证了在使用input.nextInt的情况下，如果输入字母也不会报异常

			Scanner input = new Scanner(System.in); // 该语句不能放到while循环体外部，否则会造成死循环

			if (input.hasNextInt() == true) { // input.hasNextInt() == true
				// 判断输入是否为int型
				int num = input.nextInt();

				if (0 < num && num < 10) {

					mobileCard.setCardNumber(cardNumbers[num - 1]);
					break;
				} else {
					System.out.print("输入错误！请输入（1~9）的数字:");
					continue;
				}
			} else {
				System.out.print("输入错误！请输入（1~9）的整数:");
				continue;
			}
		}
		System.out.println("1.话唠套餐  2.网虫套餐  3.超人套餐 ， 请选择套餐（输入序号）:");

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
				System.out.println("输入错误，请重新选择：");
				bol = true;
				break;
			}
		}

		System.out.println("请输入姓名：");
		String userName = input.next();
		mobileCard.setUserName(userName);
		System.out.println("请输入密码：");
		String passWord = input.next();
		mobileCard.setPassWord(passWord);
		System.out.println("请输入预存话费：");
		double money = 0.0;
		boolean flag = false;// 控制循环以及控制if语句
		do {
			if (flag == true) {
				System.out.println("您预存的话费金额不足以支付本月固定套餐资费(" + mobileCard.getSerPackage().getPrice() + "元)，请重新充值:");
			}

			while (true) {

				Scanner input = new Scanner(System.in);
				if (input.hasNextDouble() == true) {
					money = input.nextDouble();
					break;
				} else {
					System.out.println("输入错误！请重新输入");
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
		// 判断 套餐
		if (mobileCard.getSerPackage() == Common.talkPackage) {
			System.out.println("注册成功！卡号：" + mobileCard.getCardNumber() + ",用户名：" + mobileCard.getUserName() + ",当前余额为："
					+ (mobileCard.getMoney() - Common.talkPackage.getPrice()) + "元");
		} else if (mobileCard.getSerPackage() == Common.netPackage) {
			System.out.println("注册成功！卡号：" + mobileCard.getCardNumber() + ",用户名：" + mobileCard.getUserName() + ",当前余额为："
					+ (mobileCard.getMoney() - Common.netPackage.getPrice()) + "元");
		} else if (mobileCard.getSerPackage() == Common.superPackage) {
			System.out.println("注册成功！卡号：" + mobileCard.getCardNumber() + ",用户名：" + mobileCard.getUserName() + ",当前余额为："
					+ (mobileCard.getMoney() - Common.superPackage.getPrice()) + "元");
		}
		mobileCard.getSerPackage().showInfo();

		System.out.println("\n");
	}

	public static void main(String[] args) {
		// 初始化一条信息
		MobileCard mobileCard = new MobileCard("18810152602", "虎子", "998", Common.superPackage, 0, 100, 0, 0, 0);
		CardUtil.addCard(mobileCard);
		SosoMgr sosoMgr = new SosoMgr();
		sosoMgr.mainMenu();
	}

}