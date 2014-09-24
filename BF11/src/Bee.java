import java.awt.*;
import java.awt.image.*;
import java.awt.geom.AffineTransform;
import javax.swing.*;
import java.util.*;

public class Bee extends JPanel {
    
    private BufferedImage bufImage; //用于显示的缓冲区图像
    private BufferedImage originalBufImage; //原始缓冲区图像
    private Graphics2D bufImageG; //缓冲区图像的图形环境    
	private static Toolkit tk = Toolkit.getDefaultToolkit();
    /**蜜蜂当前的坐标*/
	private int id;
	private Image img;//蜜蜂的图片
	private int posX;
	private int posY;
	private boolean isAlive;
	private FlyingStatus fs;
	private int[] nextX;
	private int[] nextY;  
	String isCatched;
	
	/**蜜蜂的飞行速度*/
	private int speed = 18;
	//private int speed = 27;//@YQ原来的代码为18
	
	/**蜜蜂当前飞行方向和上一次方向，上一次方向用于限制转向范围*/
	double angle,oldAngle;
	
	/**蜜蜂的构造函数
	* @param id 蜜蜂的ID
	* @param x,y 蜜蜂身体中心的坐标
	* @param angle 蜜蜂出发时的角度
	* @param img 蜜蜂的图片
	*/
	public Bee(int id,int x, int y, double angle,boolean isAlive,Image img){
		this.id = id;
		this.posX = x;
		this.posY = y;
		this.img = img;
		this.angle = angle;
		this.isAlive = isAlive;
		fs = new FlyingStatus(id,x,y,angle,isAlive,0);
		if(fs.isAlive==true){
			BeeFarming.update(fs);//向主程序报告自己的状态
		}
		setOpaque(false);//设置JPanel透明，只显示图片，不显示底板
		//设置JPanel的大小，很重要，否则画布上看不到任何东西
		setSize(img.getWidth(null),img.getHeight(null));
		//设置JPanel在画布中的位置
		setLocation(posX-img.getWidth(null)/2,posY-img.getHeight(null)/2);//30是坐标系略微的差距
		//建立图像缓冲区（下面不需要了解细节）
		originalBufImage = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_ARGB);     //创建原始缓冲区图像
		bufImage = originalBufImage;
        bufImageG = bufImage.createGraphics(); //创建bufImage的图形环境
        bufImageG.drawImage(img, 0, 0, this); //传输源图像数据到缓冲区图像中
		ratoteImage(angle);
	}
	
    /**旋转蜜蜂身体的方向
	* @param a 蜜蜂身体旋转后的角度，范围是0-360，X 正半轴上的点向 Y 正半轴旋转
	*/
    public void ratoteImage(double a) {
        if (bufImage == null)
            return; //如果bufImage为空则直接返回
        BufferedImage filteredBufImage =new BufferedImage(img.getWidth(this) ,img.getHeight(this),BufferedImage.TYPE_INT_ARGB); //过滤后的图像
        AffineTransform transform = new AffineTransform(); //仿射变换对象
        transform.rotate(Math.toRadians(a),img.getWidth(this)/2,img.getHeight(this)/2);
		AffineTransformOp imageOp = new AffineTransformOp(transform, null);//创建仿射变换操作对象            
        imageOp.filter(originalBufImage, filteredBufImage);//过滤图像，目标图像在filteredBufImage
        bufImage = filteredBufImage; //让用于显示的缓冲区图像指向过滤后的图像
        repaint(); //重绘组件
		oldAngle = angle;//旋转后保留上一次角度记录，用于判断视野范围
		angle = a;
		//System.out.println("旋转角度："+angle);
    }
        
    /**重载容器的paintComponent()方法*/
    public void paint(Graphics g) {
        super.paintComponent(g);
        if (bufImage != null&& isAlive == true) {
            Graphics2D g2 = (Graphics2D) g;
			Color c = g2.getColor();
			Font f = g2.getFont();
            g2.drawImage(bufImage,0,0,this);    //绘制图片
			//g2.setFont(new Font("Arial",Font.PLAIN,10));
			//g2.drawString("x:"+posX, 10, 10); 
			//g2.drawString("y:"+posY, 10, 26); 
			g2.setColor(c);
			g2.setFont(f);
        }
    } 

	/**蜜蜂搜索方法，用于确定下一飞行周期的飞行路线
	*/
	public void search(){
		String strVision = BeeFarming.search(id);
		//如果碰到*为首的字符串，代表遇到了边，这里是随机顺时针旋转90度以内的角度
		if(strVision.indexOf('*')==0)
		{			
			Random ra = new Random();
			angle += ra.nextInt(90);
			ratoteImage(angle);
		}
		//如果是其它信息，则打印出来
		if(strVision.length()>0)
			System.out.println(strVision);	
		setXYs(0);
	}
	
	/**根据当前飞行角度确定下一段飞行的相对坐标序列,存放于nextX[]和nextY[]中
     * @param start 从哪个时刻开始飞行，由于蜜蜂可能预知下一轮蜂蜜是否采完，可以提前设定飞离坐标，如果不考虑
	 *  缺省为0，这样蜜蜂采完蜜后在下一个飞行周期到来前，会停留原地
	*/
	public void setXYs(int start){
		//nextX[]和nextY[]是存放下一段稳定飞行的相对坐标
		nextX = new int[9];
		nextY = new int[9];
		int deltaX = (int)(speed * (9-start)/9.0 * Math.cos(Math.toRadians(angle)));
		int deltaY = (int)(speed * (9-start)/9.0 * Math.sin(Math.toRadians(angle)));
		//这样获得的相对位移可以实现小角度变化的位移
		int dx_sum = 0;
		int dy_sum = 0;
		//如飞行周期还未用完花蜜采光，飞行继续，start表示从第几轮飞行周期停止采蜜并继续飞行
		for(int i=start;i<9;i++){
			nextX[i] = (int)(deltaX * (i+1)/ 9.0)-dx_sum;
			nextY[i] = (int)(deltaY * (i+1)/ 9.0)-dy_sum;
			dx_sum += nextX[i];
			dy_sum += nextY[i];
		}
	}
	
	/**蜜蜂按照计算好的一系列位置保持稳定飞行，在飞行期间不与外界交互
	* @param i 目前飞行到第几个已经确定的位置
	*/
	public void flying(int i){
		//x,y处是否有花蜜，如果有就采蜜，并在下一时刻停留(设next[i+1]=0)
		//蜜蜂飞到一半的时候，遇到了花，就地停留，直至用完本次飞行周期
		if(id!=9&&fs.isAlive==true&&BeeFarming.pickFlowerHoney(id)==1&&i<9){
			//System.out.println((i)+": set 0");
			nextX[i]=0;
			nextY[i]=0;
			//采蜜时旋转，增大飞行方向的不确定性
			//angle+=30;
			//ratoteImage(angle);
		}
		//询问beefarming，自己被捉住或捉住别人了？被捉住-死掉，捉住-寻找新的目标
		posX += nextX[i];
		posY += nextY[i];
		if(id==9){
			BeeFarming.killBee(9);
		}
		isAlive = getlive();
		fs = new FlyingStatus(id,posX,posY,angle,isAlive,0);
		if(fs.isAlive==true)
		BeeFarming.update(fs);//向主程序报告自己的状态
		setLocation(posX-img.getWidth(null)/2,posY-img.getHeight(null)/2);
	}
	
	public boolean getlive(){
	    isAlive=fs.isAlive;
		return isAlive;
	}
	
	public boolean live(boolean isAlive){
       
		if(isAlive=false)
		{
			setVisible(false);
			return false;
		}
		repaint();
		return true;
	}
	
}