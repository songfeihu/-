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

import ���ƶ�.Scene;

//������

public class CardUtil {

	protected static Map<String, MobileCard> cards = new HashMap<String, MobileCard>(); // ��ע�����ƶ��û��б�

	protected static Map<String, List<ConsumInfo>> consumInfos = new HashMap<String, List<ConsumInfo>>(); // ���п��ŵ����Ѽ�¼�б�

	protected static Map<Integer, Scene> scenes = new HashMap<Integer, Scene>();// ʹ�ó����б�

	protected static Scene scene0 = new Scene("ͨ��", 90, "���쵼����һ�ٷǳ���񫣬ͨ��90����");
	protected static Scene scene1 = new Scene("ͨ��", 30, "��绰�����ʣ�ͨ��30����");
	protected static Scene scene2 = new Scene("����", 5, "û���еķ������棬���Ͷ���5��");
	protected static Scene scene3 = new Scene("����", 50, "֪ͨ�����ֻ����ţ����Ͷ���50��");
	protected static Scene scene4 = new Scene("����", 1024, "����͵��С��Ƶ��ʹ������1GB");
	protected static Scene scene5 = new Scene("����", 2 * 1024, "����͵��С��Ƶ��������˯������ʹ��2GB");
	protected static Scanner input = new Scanner(System.in);

	public CardUtil() {
		super();
	}

	// ע���¿�
	public static void addCard(MobileCard card) {
		cards.put(card.getCardNumber(), card);
	}

	// ���ѳ�ֵ
	public static void chargeMoney(String number) {
		System.out.println("������Ҫ��ֵ�Ľ��(������50Ԫ)��");
		while (true) {
			double money = 0.0;
			while (true) {
				Scanner input = new Scanner(System.in);
				if (input.hasNextDouble() == true) {
					money = input.nextDouble();
					break;
				} else {
					System.out.print("�����������������:");

				}
			}
			if (money < 50) {
				System.out.println("����������50Ԫ���������룺");
				continue;
			} else {
				cards.get(number).setMoney(cards.get(number).getMoney() + money);
				System.out.println("��ֵ�ɹ�����ǰ�������Ϊ" + dataFormat(cards.get(number).getMoney()));
				break;
			}
		}

	}

	// ʹ����
	public static void userSoso(String number) {
		// ��ӳ���Map���ϵļ�ֵ��
		scenes.put(0, scene0);
		scenes.put(1, scene1);
		scenes.put(2, scene2);
		scenes.put(3, scene3);
		scenes.put(4, scene4);
		scenes.put(5, scene5);

		MobileCard card = cards.get(number); // ��ȡ�˿�����
		ServicePackage pack = card.getSerPackage(); // ��ȡ�˿������ײ�
		Random random = new Random();
		int ranNum = 0;
		int temp = 0; // ��¼�������е�ʵ����������
		do {
			ranNum = random.nextInt(6);
			Scene scene = scenes.get(ranNum); // ��ȡ��������ж�Ӧ�ĳ���
			switch (ranNum) {
			case 0:
			case 1:
				// ���Ϊ0��1��ͨ������
				// ��ȡ�ÿ������ײ��Ƿ�֧��ͨ������
				if (pack instanceof CallService) {
					// ִ��ͨ������
					System.out.println(scene.getDescription());
					CallService callService = (CallService) pack;
					try {
						temp = callService.call(scene.getData(), card);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// ���һ�����Ѽ�¼
					addConsumInfo(number, new ConsumInfo(number, scene.getType(), temp));
					break;
				} else {
					// ����ÿ��ײͲ�֧��ͨ�����ܣ����������������ѡ����������
					continue;
				}
			case 2:
			case 3:
				// ���2��3�����ų���
				// ��ȡ�ÿ������ײ��Ƿ�֧�ֶ���
				if (pack instanceof SendService) {
					// ִ�ж��ŷ���
					System.out.println(scene.getDescription());
					SendService sendService = (SendService) pack;
					try {
						temp = sendService.send(scene.getData(), card);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// ���һ�����Ѽ�¼
					addConsumInfo(number, new ConsumInfo(number, scene.getType(), temp));
					break;
				} else {
					// ����ÿ��ײͲ�֧�ֶ��Ź��ܣ����������������ѡ����������
					continue;
				}
			case 4:
			case 5:
				// ��ȡ�ÿ������ײ��Ƿ�֧������
				if (pack instanceof NetService) {
					// ִ����������
					System.out.println(scene.getDescription());
					NetService netService = (NetService) pack;
					try {
						temp = netService.netPlay(scene.getData(), card);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// ���һ�����Ѽ�¼
					addConsumInfo(number, new ConsumInfo(number, scene.getType(), temp));
					break;
				} else {
					// ����ÿ��ײͲ�֧���������ܣ����������������ѡ����������
					continue;
				}
			}
			break;
		} while (true);
	}

	// �ʷ�˵��
	public static void showDescription() {
		FileReader fr = null;
		BufferedReader br = null;
		try {
			// ����һ��FileReader����
			fr = new FileReader("e:/�ײ��ʷ�˵��.txt");
			// ����һ��BufferedReader����
			br = new BufferedReader(fr);
			// ��ȡһ������
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

	// �����˵���ѯ
	public static void showAmountDetail(String number) {
		MobileCard mobileCard = cards.get(number);
		System.out.println("���Ŀ���Ϊ��" + mobileCard.getCardNumber());
		System.out.println("�����˵����£�");
		System.out.println("�ײ��ʷѣ�" + dataFormat(mobileCard.getSerPackage().getPrice()) + "Ԫ" + "\t�ϼ����ѣ�"
				+ dataFormat(mobileCard.getConsumAmount()) + "Ԫ" + "\t�˻���" + dataFormat(mobileCard.getMoney()) + "Ԫ");
	}

	// �ײ�������ѯ
	public static void showRemainDetail(String searchNumber) {
		MobileCard mobileCard = cards.get(searchNumber);
		int remainTalkTime;
		int remainSmsCount;
		int remainFlow;
		StringBuffer meg = new StringBuffer();
		meg.append("���Ŀ�����" + searchNumber + ",�ײ���ʣ�ࣺ\n");
		ServicePackage pack = mobileCard.getSerPackage();
		if (pack instanceof TalkPackage) {
			// ����ת��Ϊ�������
			TalkPackage cardPack = (TalkPackage) pack;
			// �����ײ�,��ѯ�ײ���ʣ���ͨ��ʱ���Ͷ�����
			remainTalkTime = cardPack.getTalkTime() > mobileCard.getRealTalkTime()
					? cardPack.getTalkTime() - mobileCard.getRealTalkTime() : 0;
			meg.append("ͨ��ʱ����" + remainTalkTime + "����\n");
			remainSmsCount = cardPack.getSmsCount() > mobileCard.getRealSMSCount()
					? cardPack.getSmsCount() - mobileCard.getRealSMSCount() : 0;
			meg.append("����������" + remainSmsCount + "��");
		} else if (pack instanceof NetPackage) {
			// ����ת��Ϊ�������
			NetPackage cardPack = (NetPackage) pack;
			// �����ײͲ�ѯ��������
			remainFlow = cardPack.getFlow() > mobileCard.getRealFlow() ? cardPack.getFlow() - mobileCard.getRealFlow()
					: 0;
			meg.append("����������" + dataFormat(remainFlow * 1.0 / 1024) + "GB");
		} else if (pack instanceof SuperPackage) {
			// ����ת��Ϊ���˶���
			SuperPackage cardPack = (SuperPackage) pack;
			// �����ײͲ�ѯͨ��ʱ������������������������
			remainTalkTime = cardPack.getTalkTime() > mobileCard.getRealTalkTime()
					? cardPack.getTalkTime() - mobileCard.getRealTalkTime() : 0;
			meg.append("ͨ��ʱ����" + remainTalkTime + "����\n");
			remainFlow = cardPack.getFlow() > mobileCard.getRealFlow() ? cardPack.getFlow() - mobileCard.getRealFlow()
					: 0;
			meg.append("����������" + dataFormat(remainFlow * 1.0 / 1024) + "GB");
			remainSmsCount = cardPack.getSmsCount() > mobileCard.getRealSMSCount()
					? cardPack.getSmsCount() - mobileCard.getRealSMSCount() : 0;
			meg.append("����������" + remainSmsCount + "��");
		}
		System.out.println(meg);

	}

	// ��ӡ�����굥
	public static void printAmountDetail(String number) {
		Writer fileWriter = null;

		try {

			fileWriter = new FileWriter(number + "���Ѽ�¼.txt");
			Set<String> numbers = consumInfos.keySet();
			Iterator<String> it = numbers.iterator();
			List<ConsumInfo> infos = new ArrayList<ConsumInfo>();
			infos = consumInfos.get(number);
			// �洢ָ�������������Ѽ�¼
			// ���������б����Ƿ���ڴ˿��ŵ����Ѽ�¼���ǣ�true ��false
			boolean isExist = false;
			while (it.hasNext()) {
				String numberKey = it.next();
				if (number.equals(numberKey)) {
					isExist = true;
				}
			}
			if (isExist) {
				StringBuffer content = new StringBuffer("***********" + number + "���Ѽ�¼************\n");
				content.append("���\t����\t���ݣ�ͨ�������ӣ�/������MB��/���ţ�������\n");
				for (int i = 0; i < infos.size(); i++) {
					ConsumInfo info = infos.get(i);
					content.append((i + 1) + ".\t" + info.getType() + "\t" + info.getConsumData() + "\n");
				}

				fileWriter.write(content.toString());
				fileWriter.flush();
				System.out.println("��Ϣ��¼��ӡ��ϣ�");
			} else {
				System.out.println("�Բ��𣬲����ڴ˺�������Ѽ�¼�����ܹ���ӡ��");
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

	// �ײͱ��
	public static void changingPack(String number) {
		System.out.println("1.�����ײ� 2.�����ײ� 3.�����ײ� ����ѡ��(���)��");
		int packNum = input.nextInt();
		switch (packNum) {
		case 1: // ѡ�������ײ�Ϊ�����ײ�
			if (cards.get(number).getSerPackage() instanceof TalkPackage) {
				System.out.println("�Բ������Ѿ��Ǹ��ײ��û������軻�ײͣ�");
			} else {
				if (cards.get(number).getMoney() < cards.get(number).getSerPackage().getPrice()) {// ���ʣ����ò���֧���µ��ײͷ���

					System.out.println("�Բ���,���ĵ�������֧���µ��ײͱ����ʷѣ����ֵ���ٰ���ҵ��");

				} else {

					cards.get(number).setRealSMSCount(0);
					cards.get(number).setRealTalkTime(0);
					cards.get(number).setRealFlow(0);
					cards.get(number).setSerPackage(Common.talkPackage);
					System.out.println("�ײ͸����ɹ���");
					Common.talkPackage.showInfo();

				}
			}

			break;

		case 2: // ѡ�������ײ�Ϊ�����ײ�
			if (cards.get(number).getSerPackage() instanceof NetPackage) {
				System.out.println("�Բ������Ѿ��Ǹ��ײ��û������軻�ײͣ�");
			} else {
				if (cards.get(number).getMoney() < cards.get(number).getSerPackage().getPrice()) {// ���ʣ����ò���֧���µ��ײͷ���
					System.out.println("�Բ���,���ĵ�������֧���µ��ײͱ����ʷѣ����ֵ���ٰ���ҵ��");
				} else {
					cards.get(number).setRealSMSCount(0);
					cards.get(number).setRealTalkTime(0);
					cards.get(number).setRealFlow(0);
					cards.get(number).setSerPackage(Common.netPackage);
					System.out.println("�ײ͸����ɹ���");
					Common.netPackage.showInfo();

				}
			}

			break;

		case 3:// ѡ�������ײ�Ϊ�����ײ�
			if (cards.get(number).getSerPackage() instanceof SuperPackage) {
				System.out.println("�Բ������Ѿ��Ǹ��ײ��û������軻�ײͣ�");
			} else {
				if (cards.get(number).getMoney() < cards.get(number).getSerPackage().getPrice()) {// ���ʣ����ò���֧���µ��ײͷ���
					System.out.println("�Բ���,���ĵ�������֧���µ��ײͱ����ʷѣ����ֵ���ٰ���ҵ��");
				} else {
					cards.get(number).setRealSMSCount(0);
					cards.get(number).setRealTalkTime(0);
					cards.get(number).setRealFlow(0);
					cards.get(number).setSerPackage(Common.superPackage);
					System.out.println("�ײ͸����ɹ���");
					Common.superPackage.showInfo();

				}
			}
			break;
		}
	}

	// ��������
	public static void delCard(String number) {
		Set<String> numberKeys = cards.keySet();
		Iterator<String> it = numberKeys.iterator();
		while (it.hasNext()) {
			String numberKey = it.next();
			if (numberKey.equals(number)) {
				cards.remove(numberKey);
				// ��������仰���ܻᱨ�쳣���*
				System.out.println("����" + number + "���������ɹ�\nллʹ�ã�");
			} else {
				System.out.println("�����˿�ʧ�ܣ�");
			}
		}
	}

	// ���ݿ��ź�������֤�ÿ��Ƿ�ע��
	public static boolean isExistCard(String number, String passWord) {
		if (cards.size() != 0) {
			Set<String> numberKeys = cards.keySet();
			// System.out.println(numberKeys);
			Iterator<String> its = numberKeys.iterator();
			while (its.hasNext()) {
				String numberKey = its.next();
				MobileCard mobileCard = cards.get(numberKey); // ����keyȡ����Ӧ��ֵ
				if (number.trim().equals(numberKey.trim()) && passWord.trim().equals(mobileCard.getPassWord().trim())) {
					System.out.println("���û����ڣ����ʺ����붼��ȷ");
					return true;
				} else if (number.trim().equals(numberKey.trim()) == true
						&& passWord.trim().equals(mobileCard.getPassWord().trim()) == false) {
					System.out.println("���û����ڣ����������");
					return false;
				}

			}
			System.out.println("���û�������");
			return false;
		} else {
			System.out.println("cards����Ϊ�գ��������û���");
			return false;
		}

	}

	// ���ݿ�����֤�ÿ����Ƿ�ע��
	public static boolean isExistCard(String number) {
		if (cards.size() != 0) {
			Set<String> numberKeys = cards.keySet();
			Iterator<String> its = numberKeys.iterator();
			while (its.hasNext()) {
				if (number.equals(its.next())) {
					// System.out.println("���û��Ѿ�ע�ᣡ");
					return true;
				}

			}
			System.out.println("���û������ڣ�");
			return false;
		} else {
			System.out.println("cards����Ϊ�գ��������û���");
			return false;
		}
	}

	// �����������
	public static String createNumber() {
		Random random = new Random();
		// ��¼�����û����Ƿ���ڴ˿����û� �ǣ�true ��false
		boolean isExist = false;
		String number = "";
		int temp = 0;
		do {
			isExist = false;// ��־λ����Ϊfalse,���ڿ�������ѭ��
			// ���ɵ��������8λ������С��10000000��������������
			// ��ͷ��д��δ��룬ִ��Ч��̫��
			do {
				temp = random.nextInt(100000000);
			} while (temp < 10000000);
			// ����֮ǰ��ǰ��ӡ�139��
			number = "139" + temp;
			// �������û��Ŀ��űȽϣ��������ظ���
			if (cards != null) { // �۸��ж� ���� �·���һ��ᱨ��ָ���쳣
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

	// ����ָ�������Ŀ����б� (��ͷ�����Ƿ����ͨ�����ؼ����б�ķ�ʽ��ʾ)
	public static String[] getNewNumbers(int count) {
		String[] strs = new String[count];
		for (int i = 0; i < count; i++) {
			strs[i] = createNumber(); // ͨ��������ɸ�strs[i]��ֵ
		}
		return strs;
	}

	// ���ָ�����ŵ����Ѽ�¼
	public static void addConsumInfo(String number, ConsumInfo info) {
		if (consumInfos.containsKey(number)) {
			consumInfos.get(number).add(info);
		} else {
			List<ConsumInfo> list = new ArrayList<ConsumInfo>();
			list.add(info);
			consumInfos.put(number, list);
		}
	}

	// ��double���ݸ�ʽ�����
	public static String dataFormat(double data) {
		DecimalFormat formatData = new DecimalFormat("#0.0");
		return formatData.format(data);
	}
}