import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Point;
import javax.swing.JPanel;

public class Flower extends JPanel {
	Image img;//花的图片
	BufferedImage bufImage; //用于显示的缓冲区图像
	BufferedImage originalBufImage; //原始缓冲区图像
	Graphics2D bufImageG; //缓冲区图像的图形环境    
	/**花的坐标*/
	private int posX;
	private int posY;
	private int volumn;
	/**花的生成时间*/
	private int time;
	/**花所在的面板*/
	private BackGroundJP pGround;
	
	/**花的构造函数
	* @param x,y 花中心的坐标
	* @param volumn 花蜜容量
	* @param img 花的图像
	*/
	public Flower(int time,int x, int y, int volumn,Image img,BackGroundJP pGround){
		this.pGround = pGround;
	    this.time = time;
		this.posX = x;
		this.posY = y;
		this.img = img;
		this.volumn = volumn;
		setOpaque(false);//设置JPanel透明，只显示图片，不显示底板
		//设置JPanel的大小，很重要，否则画布上看不到任何东西
		setSize(64,64);
		//设置JPanel在画布中的位置
		setLocation(posX-32,posY-32);
		originalBufImage = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_ARGB);     //创建原始缓冲区图像
		bufImage = originalBufImage;
	    bufImageG = bufImage.createGraphics(); //创建bufImage的图形环境
	    bufImageG.drawImage(img,(56-img.getWidth(null))/2,(56-img.getHeight(null))/2, this); //传输源图像数据到缓冲区图像中
	}
	
	/**重载容器的paintComponent()方法*/
	public void paint(Graphics g) {
		super.paintComponent(g);
		
		//只有花蜜数量大于0，才绘制花
		if (bufImage != null&&volumn>0) {
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(bufImage,3,3,this);    //绘制图片
			Color c = g2.getColor();
			Font f = g2.getFont();
			
			//g2.setColor(new Color(255,255,255));
			//g2.drawRect(0,0,63,63);
			g2.setColor(new Color(255,255,0));
			g2.fillOval(28,28,8,8);	
			//g2.setFont(new Font("Arial",Font.PLAIN,10));
			//g2.drawString(volumn+"("+posX+","+posY+")", 5, 12); 
			g2.setColor(c);
			g2.setFont(f);
		}      
	} 
	
	/**每次被蜜蜂消耗的蜂蜜，如果蜂蜜为0，花就不显示
	* @param sub 蜜蜂一次采蜜的数量
	* @return 是否可以再采蜜（true-还有蜜，false-最后一定采完了）
	*/
	public boolean consume(int sub){	
		volumn -= sub;
		if(volumn<=0)
		{
			setVisible(false);
			return false;
		}
		repaint();
		return true;
	}
    
	/**获得剩余花蜜的容量
	* @return 返回花蜜的量
	*/
	public int getVolumn(){
		return volumn;
	}
	
	/**获得花的位置
	* @return 返回花蜜的坐标
	*/
	public Point getPosition(){
		return new Point(posX,posY);
	}
	
	/**获得花的生成时间
	* @return 返回花的轮数
	*/
	public int getTime(){
		return time;
	}
	
	/**获得面板
	* @return 返回pGround
	*/
	public BackGroundJP getPGround(){
		return pGround;
	}
}


