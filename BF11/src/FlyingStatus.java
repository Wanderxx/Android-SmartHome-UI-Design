
public class FlyingStatus{
	int id;
	int x;
	int y;
	double angle;
	boolean isAlive;
	int status;
	
	/**飞行状态的构造函数
	* @param id 蜜蜂的id
	* @param x,y 当前的坐标
	* @param angle 蜜蜂飞行的角度（0-360度）
	* @param status 状态
	*/
	public FlyingStatus(int id,int x,int y,double angle,boolean isAlive,int status){
		this.id = id;
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.isAlive = isAlive;
		this.status = status;
	}
	public boolean getisAlive(){
		return isAlive;
	}
		
}