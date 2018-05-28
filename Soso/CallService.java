package Soso;

// 接口 通话服务

	public interface CallService {
		
	public abstract int call(int minCount, MobileCard card) throws Exception;
}