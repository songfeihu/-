package Soso;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import 嗖嗖移动.Scene;

//工具类

public class CardUtil {

	protected static Map<String, MobileCard> cards = new HashMap<String, MobileCard>(); // 已注册嗖嗖移动用户列表

	protected static Map<String, List<ConsumInfo>> consumInfos = new HashMap<String, List<ConsumInfo>>(); // 所有卡号的消费记录列表

	protected static Map<Integer, Scene> scenes = new HashMap<Integer, Scene>();// 使用场景列表

	protected static Scene scene0 = new Scene("通话", 90, "把领导骂了一顿非常过瘾，通话90分钟");
	protected static Scene scene1 = new Scene("通话", 30, "打电话给金沂，通话30分钟");
	protected static Scene scene2 = new Scene("短信", 5, "没事闲的发短信玩，发送短信5条");
	protected static Scene scene3 = new Scene("短信", 50, "通知朋友手机换号，发送短信50条");
	protected static Scene scene4 = new Scene("上网", 1024, "晚上偷看小视频，使用流量1GB");
	protected static Scene scene5 = new Scene("上网", 2 * 1024, "晚上偷看小视频，不留神睡着啦！使用2GB");
	protected static Scanner input = new Scanner(System.in);

	public CardUtil() {
		super();
	}

	// 注册新卡
	public static void addCard(MobileCard card) {
		cards.put(card.getCardNumber(), card);
	}

	// 话费充值
	public static void chargeMoney(String number) {
		System.out.println("请输入要充值的金额(不少于50元)：");
		while (true) {
			double money = 0.0;
			while (true) {
				Scanner input = new Scanner(System.in);
				if (input.hasNextDouble() == true) {
					money = input.nextDouble();
					break;
				} else {
					System.out.print("输入错误！请重新输入:");

				}
			}
			if (money < 50) {
				System.out.println("输入金额少于50元请重新输入：");
				continue;
			} else {
				cards.get(number).setMoney(cards.get(number).getMoney() + money);
				System.out.println("充值成功，当前话费余额为" + dataFormat(cards.get(number).getMoney()));
				break;
			}
		}

	}

	// 使用嗖嗖
	public static void userSoso(String number) {
		// 添加场景Map集合的键值对
		scenes.put(0, scene0);
		scenes.put(1, scene1);
		scenes.put(2, scene2);
		scenes.put(3, scene3);
		scenes.put(4, scene4);
		scenes.put(5, scene5);

		MobileCard card = cards.get(number); // 获取此卡对象
		ServicePackage pack = card.getSerPackage(); // 获取此卡所属套餐
		Random random = new Random();
		int ranNum = 0;
		int temp = 0; // 记录各场景中的实际消费数据
		do {
			ranNum = random.nextInt(6);
			Scene scene = scenes.get(ranNum); // 获取该序号所有对应的场景
			switch (ranNum) {
			case 0:
			case 1:
				// 序号为0或1的通话场景
				// 获取该卡所属套餐是否支持通话功能
				if (pack instanceof CallService) {
					// 执行通话方法
					System.out.println(scene.getDescription());
					CallService callService = (CallService) pack;
					try {
						temp = callService.call(scene.getData(), card);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// 添加一条消费记录
					addConsumInfo(number, new ConsumInfo(number, scene.getType(), temp));
					break;
				} else {
					// 如果该卡套餐不支持通话功能，则重新生成随机数选择其他场景
					continue;
				}
			case 2:
			case 3:
				// 序号2或3发短信场景
				// 获取该卡所属套餐是否支持短信
				if (pack instanceof SendService) {
					// 执行短信方法
					System.out.println(scene.getDescription());
					SendService sendService = (SendService) pack;
					try {
						temp = sendService.send(scene.getData(), card);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// 添加一条消费记录
					addConsumInfo(number, new ConsumInfo(number, scene.getType(), temp));
					break;
				} else {
					// 如果该卡套餐不支持短信功能，则重新生成随机数选择其他场景
					continue;
				}
			case 4:
			case 5:
				// 获取该卡所属套餐是否支持上网
				if (pack instanceof NetService) {
					// 执行上网方法
					System.out.println(scene.getDescription());
					NetService netService = (NetService) pack;
					try {
						temp = netService.netPlay(scene.getData(), card);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// 添加一条消费记录
					addConsumInfo(number, new ConsumInfo(number, scene.getType(), temp));
					break;
				} else {
					// 如果该卡套餐不支持上网功能，则重新生成随机数选择其他场景
					continue;
				}
			}
			break;
		} while (true);
	}

	// 资费说明
	public static void showDescription() {
		FileReader fr = null;
		BufferedReader br = null;
		try {
			// 创建一个FileReader对象
			fr = new FileReader("e:/套餐资费说明.txt");
			// 创建一个BufferedReader对象
			br = new BufferedReader(fr);
			// 读取一行数据
			String line = null;

			while ((line = br.readLine()) != null) {

				System.out.println(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (fr != null) {
					fr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 本月账单查询
	public static void showAmountDetail(String number) {
		MobileCard mobileCard = cards.get(number);
		System.out.println("您的卡号为：" + mobileCard.getCardNumber());
		System.out.println("当月账单如下：");
		System.out.println("套餐资费：" + dataFormat(mobileCard.getSerPackage().getPrice()) + "元" + "\t合计消费："
				+ dataFormat(mobileCard.getConsumAmount()) + "元" + "\t账户余额：" + dataFormat(mobileCard.getMoney()) + "元");
	}

	// 套餐余量查询
	public static void showRemainDetail(String searchNumber) {
		MobileCard mobileCard = cards.get(searchNumber);
		int remainTalkTime;
		int remainSmsCount;
		int remainFlow;
		StringBuffer meg = new StringBuffer();
		meg.append("您的卡号是" + searchNumber + ",套餐内剩余：\n");
		ServicePackage pack = mobileCard.getSerPackage();
		if (pack instanceof TalkPackage) {
			// 向下转型为话唠对象
			TalkPackage cardPack = (TalkPackage) pack;
			// 话唠套餐,查询套餐内剩余的通话时长和短信数
			remainTalkTime = cardPack.getTalkTime() > mobileCard.getRealTalkTime()
					? cardPack.getTalkTime() - mobileCard.getRealTalkTime() : 0;
			meg.append("通话时长：" + remainTalkTime + "分钟\n");
			remainSmsCount = cardPack.getSmsCount() > mobileCard.getRealSMSCount()
					? cardPack.getSmsCount() - mobileCard.getRealSMSCount() : 0;
			meg.append("短信条数：" + remainSmsCount + "条");
		} else if (pack instanceof NetPackage) {
			// 向下转型为网虫对象
			NetPackage cardPack = (NetPackage) pack;
			// 网虫套餐查询上网流量
			remainFlow = cardPack.getFlow() > mobileCard.getRealFlow() ? cardPack.getFlow() - mobileCard.getRealFlow()
					: 0;
			meg.append("上网流量：" + dataFormat(remainFlow * 1.0 / 1024) + "GB");
		} else if (pack instanceof SuperPackage) {
			// 向下转型为超人对象
			SuperPackage cardPack = (SuperPackage) pack;
			// 超人套餐查询通话时长、上网流量、短信条数。
			remainTalkTime = cardPack.getTalkTime() > mobileCard.getRealTalkTime()
					? cardPack.getTalkTime() - mobileCard.getRealTalkTime() : 0;
			meg.append("通话时长：" + remainTalkTime + "分钟\n");
			remainFlow = cardPack.getFlow() > mobileCard.getRealFlow() ? cardPack.getFlow() - mobileCard.getRealFlow()
					: 0;
			meg.append("上网流量：" + dataFormat(remainFlow * 1.0 / 1024) + "GB");
			remainSmsCount = cardPack.getSmsCount() > mobileCard.getRealSMSCount()
					? cardPack.getSmsCount() - mobileCard.getRealSMSCount() : 0;
			meg.append("短信条数：" + remainSmsCount + "条");
		}
		System.out.println(meg);

	}

	// 打印消费详单
	public static void printAmountDetail(String number) {
		Writer fileWriter = null;

		try {

			fileWriter = new FileWriter(number + "消费记录.txt");
			Set<String> numbers = consumInfos.keySet();
			Iterator<String> it = numbers.iterator();
			List<ConsumInfo> infos = new ArrayList<ConsumInfo>();
			infos = consumInfos.get(number);
			// 存储指定卡的所有消费记录
			// 现有消费列表中是否存在此卡号的消费记录，是：true 否：false
			boolean isExist = false;
			while (it.hasNext()) {
				String numberKey = it.next();
				if (number.equals(numberKey)) {
					isExist = true;
				}
			}
			if (isExist) {
				StringBuffer content = new StringBuffer("***********" + number + "消费记录************\n");
				content.append("序号\t类型\t数据（通话（分钟）/上网（MB）/短信（条））\n");
				for (int i = 0; i < infos.size(); i++) {
					ConsumInfo info = infos.get(i);
					content.append((i + 1) + ".\t" + info.getType() + "\t" + info.getConsumData() + "\n");
				}

				fileWriter.write(content.toString());
				fileWriter.flush();
				System.out.println("消息记录打印完毕！");
			} else {
				System.out.println("对不起，不存在此号码的消费记录，不能够打印！");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 套餐变更
	public static void changingPack(String number) {
		System.out.println("1.话唠套餐 2.网虫套餐 3.超人套餐 ：请选择(序号)：");
		int packNum = input.nextInt();
		switch (packNum) {
		case 1: // 选择变更的套餐为话唠套餐
			if (cards.get(number).getSerPackage() instanceof TalkPackage) {
				System.out.println("对不起，您已经是该套餐用户，无需换套餐！");
			} else {
				if (cards.get(number).getMoney() < cards.get(number).getSerPackage().getPrice()) {// 如果剩余费用不够支持新的套餐费用

					System.out.println("对不起,您的的余额不足以支付新的套餐本月资费，请充值后再办理业务！");

				} else {

					cards.get(number).setRealSMSCount(0);
					cards.get(number).setRealTalkTime(0);
					cards.get(number).setRealFlow(0);
					cards.get(number).setSerPackage(Common.talkPackage);
					System.out.println("套餐更换成功！");
					Common.talkPackage.showInfo();

				}
			}

			break;

		case 2: // 选择变更的套餐为网虫套餐
			if (cards.get(number).getSerPackage() instanceof NetPackage) {
				System.out.println("对不起，您已经是该套餐用户，无需换套餐！");
			} else {
				if (cards.get(number).getMoney() < cards.get(number).getSerPackage().getPrice()) {// 如果剩余费用不够支持新的套餐费用
					System.out.println("对不起,您的的余额不足以支付新的套餐本月资费，请充值后再办理业务！");
				} else {
					cards.get(number).setRealSMSCount(0);
					cards.get(number).setRealTalkTime(0);
					cards.get(number).setRealFlow(0);
					cards.get(number).setSerPackage(Common.netPackage);
					System.out.println("套餐更换成功！");
					Common.netPackage.showInfo();

				}
			}

			break;

		case 3:// 选择变更的套餐为超人套餐
			if (cards.get(number).getSerPackage() instanceof SuperPackage) {
				System.out.println("对不起，您已经是该套餐用户，无需换套餐！");
			} else {
				if (cards.get(number).getMoney() < cards.get(number).getSerPackage().getPrice()) {// 如果剩余费用不够支持新的套餐费用
					System.out.println("对不起,您的的余额不足以支付新的套餐本月资费，请充值后再办理业务！");
				} else {
					cards.get(number).setRealSMSCount(0);
					cards.get(number).setRealTalkTime(0);
					cards.get(number).setRealFlow(0);
					cards.get(number).setSerPackage(Common.superPackage);
					System.out.println("套餐更换成功！");
					Common.superPackage.showInfo();

				}
			}
			break;
		}
	}

	// 办理退网
	public static void delCard(String number) {
		Set<String> numberKeys = cards.keySet();
		Iterator<String> it = numberKeys.iterator();
		while (it.hasNext()) {
			String numberKey = it.next();
			if (numberKey.equals(number)) {
				cards.remove(numberKey);
				// 下面这这句话可能会报异常标记*
				System.out.println("卡号" + number + "办理退网成功\n谢谢使用！");
			} else {
				System.out.println("办理退卡失败！");
			}
		}
	}

	// 根据卡号和密码验证该卡是否注册
	public static boolean isExistCard(String number, String passWord) {
		if (cards.size() != 0) {
			Set<String> numberKeys = cards.keySet();
			// System.out.println(numberKeys);
			Iterator<String> its = numberKeys.iterator();
			while (its.hasNext()) {
				String numberKey = its.next();
				MobileCard mobileCard = cards.get(numberKey); // 根据key取出对应的值
				if (number.trim().equals(numberKey.trim()) && passWord.trim().equals(mobileCard.getPassWord().trim())) {
					System.out.println("该用户存在，且帐号密码都正确");
					return true;
				} else if (number.trim().equals(numberKey.trim()) == true
						&& passWord.trim().equals(mobileCard.getPassWord().trim()) == false) {
					System.out.println("该用户存在，但密码错误");
					return false;
				}

			}
			System.out.println("该用户不存在");
			return false;
		} else {
			System.out.println("cards集合为空，不存在用户！");
			return false;
		}

	}

	// 根据卡号验证该卡号是否注册
	public static boolean isExistCard(String number) {
		if (cards.size() != 0) {
			Set<String> numberKeys = cards.keySet();
			Iterator<String> its = numberKeys.iterator();
			while (its.hasNext()) {
				if (number.equals(its.next())) {
					// System.out.println("该用户已经注册！");
					return true;
				}

			}
			System.out.println("该用户不存在！");
			return false;
		} else {
			System.out.println("cards集合为空，不存在用户！");
			return false;
		}
	}

	// 生成随机卡号
	public static String createNumber() {
		Random random = new Random();
		// 记录现有用户中是否存在此卡号用户 是：true 否：false
		boolean isExist = false;
		String number = "";
		int temp = 0;
		do {
			isExist = false;// 标志位重置为false,用于控制外重循环
			// 生成的随机数是8位，不能小于10000000，否则重新生成
			// 回头重写这段代码，执行效率太低
			do {
				temp = random.nextInt(100000000);
			} while (temp < 10000000);
			// 生成之前，前面加“139”
			number = "139" + temp;
			// 和现有用户的卡号比较，不能是重复的
			if (cards != null) { // 价格判断 否则 下方的一句会报空指针异常
				Set<String> cardNumbers = cards.keySet();
				for (String cardNumber : cardNumbers) {
					if (number.equals(cardNumber)) {
						isExist = true;
						break;
					}
				}
			}

		} while (isExist);
		return number;
	}

	// 生成指定个数的卡号列表 (回头尝试是否可以通过返回集合列表的方式显示)
	public static String[] getNewNumbers(int count) {
		String[] strs = new String[count];
		for (int i = 0; i < count; i++) {
			strs[i] = createNumber(); // 通过随机生成给strs[i]赋值
		}
		return strs;
	}

	// 添加指定卡号的消费记录
	public static void addConsumInfo(String number, ConsumInfo info) {
		if (consumInfos.containsKey(number)) {
			consumInfos.get(number).add(info);
		} else {
			List<ConsumInfo> list = new ArrayList<ConsumInfo>();
			list.add(info);
			consumInfos.put(number, list);
		}
	}

	// 将double数据格式化输出
	public static String dataFormat(double data) {
		DecimalFormat formatData = new DecimalFormat("#0.0");
		return formatData.format(data);
	}
}