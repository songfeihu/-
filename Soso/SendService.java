package Soso;

// 接口 短信服务

public interface SendService {
	public abstract int send(int count, MobileCard card) throws Exception;
}