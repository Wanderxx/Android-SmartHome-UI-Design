import java.awt.Image;
import java.util.Random;
import java.util.Vector;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wanderxx
 */
public class Hornet extends Bee{
	private int id;
    private boolean dead;
    private int trackStep = 0;
    private int presentTrackNum = 0;
    private int recentTrackNum = -1;
    private int presentFlowerVolumn = 0;
    private int recentFlowerVolumn= -1;
    private boolean presentFlag = false;
    private boolean killFlag = false;
    private int stillAlive =3;
    private double nextAngle = 0;
    private int time = 0;
    private int SpiralDirection = -1;
    private int headacheAvoid = 0;
    private double lid = -1;
    private double daa=0;
    private final int MAXTRACKSTEP = 400;
	public Hornet(int id,int x, int y, double angle,boolean isAlive,Image img){
		super(id,x,y,angle,isAlive,img);
		this.id = id;
	}
        
    @Override
        public void search(){
		String strVision = BeeFarming.search(id);
		System.out.println(strVision);
		String[] strs = strVision.split("~");
                time ++;
        int flag=0;
        int i = 0;       
        Vector<Double> temp = new Vector<Double>();
	for(i=0;i<strs.length;i++){
            if((strs[i].indexOf('+')==0)){
		String straim=strs[i];
		int start = straim.indexOf('(');
		int end=straim.indexOf(',');
		 String aid=straim.substring(start+1, end);
                  double id = new Double(aid).doubleValue();
                temp.add((double)i);
                temp.add(id);
            }
	} 
                
	if(temp.size()!=0){//遇到蜜蜂处理策略         
                                  presentFlag = true;
                                  headacheAvoid = 0;
                                  time=0;
                                  int j=1;
                                		
                                    i = (int) Math.floor(temp.get(0));
                                    for(j=1;j<temp.size();j+=2){
                                        if(temp.get(j) ==lid){
                                            i=(int) Math.floor(temp.get(j-1));
                                            break;
                                        }                                      
                                    }			     
			       String  straim=strs[i];                          
                               int start = straim.indexOf('(');
                               int end=straim.indexOf(',');
                               String aid=straim.substring(start+1, end);
                               straim=straim.substring(end+1);
                               end=straim.indexOf(',');
                               String aa=straim.substring(0, end);
                               straim=straim.substring(end+1);
                               end=straim.indexOf(')');
                               String aangle=straim.substring(0, end);
                               double da = new Double(aa).doubleValue();
                               daa=da;
                               double ba = new Double(aangle).doubleValue();
                               ba = ba%360;
                               presentTrackNum = Integer.parseInt(aid);
                               lid =(double) presentTrackNum;
                               if(presentTrackNum!=recentTrackNum){
                            	   trackStep= 0;
                            	   recentTrackNum= presentTrackNum;
                               }
                               else trackStep++;
                               if(trackStep<=MAXTRACKSTEP){                                                                                  
                         if(angle!=specialff(strVision, ba, da)){//花蜂双遇
                                 
                                   ratoteImage(specialff(strVision,ba,da));
                                   
                               }  
                            else  if(strVision.indexOf('*')!=-1){ 
                                if(Math.abs(da-ba)>90){//初次相遇
                                   angle=da; 
                                   ratoteImage(angle);
                               }
                                else{
                                   ratoteImage(specialdd(strVision)); 
                                }
                               }                        
                            else{
                                
                               if(Math.abs(da-ba)>90){
                                   angle=da; 
                                   ratoteImage(angle);
                               }                     
                               else if(Math.abs(da-ba)<=90){                                 
                                   if(Math.abs(da-ba)<=10&&flag<3){
                                       flag++;
                                       da=da+(ba-da)/2;
                                       angle=da; 
                                       ratoteImage(angle);
                                     }
                                   else if(flag==3){
                                      flag=0;
                                      if((ba-da)>0){
                                       da=da+(ba-da)/2+10;
                                       angle=da;
                                       System.out.println("冒险");
                                        ratoteImage(angle);
                                      	}
                                      else if((ba-da)<0){
                                       da=da+(ba-da)/2-10;
                                       angle=da;
                                       System.out.println("冒险");
                                       ratoteImage(angle);
                                   		} 
                                   	}
                                   	else{
                                       da=da+(ba-da)/2;
                                       angle=da; 
                                       ratoteImage(angle);
                                        }
                                   }
                               }                      
                               }
                               else{
                            	   trackStep=0;
                            	   ratoteImage((ba>90)?(ba-90):(ba+90));
                               }
                               setXYs(0);
                               return;
		}                  
                if(presentFlag==true&&killFlag==false)//防跟丢策略
                {
                    double lst=daa;
                    headacheAvoid++;
                    if(headacheAvoid==3){
                        headacheAvoid = 0;
                        presentFlag=false;
                       ratoteImage((angle+180)%360); 
                       setXYs(0);                  
                       return;
                    }
                    if(headacheAvoid==2) {
                        
                        if(lst>=45&&lst<=135 || lst>=225&&lst<=315){
                        ratoteImage((angle+90)%360);
                        setXYs(0);                  
                        return;}
                        else{
                            ratoteImage((angle-90)%360);
                        setXYs(0);                  
                        return;
                        }
                    }
                    else{
                    ratoteImage((angle+180)%360);
                    setXYs(0);                  
                    return;
                    }
                }
                 presentFlag = false;
                 killFlag = false;
               
			 for(i=0;i<strs.length;i++){
                         if(strs[i].indexOf('-')==0)//遇花策略
			{         
                            if(stillAlive==1){
                                time=0;
                            }                                 
                            Random ra=new Random();
				String strTmp = strs[i];
				int start = strTmp.indexOf('(');
				int end = strTmp.indexOf(',');
				String s = strTmp.substring(start+1,end);
				int honey = new Integer(s).intValue();
				presentFlowerVolumn = honey;
				strTmp = strTmp.substring(end+1);
				end = strTmp.indexOf(',')!=-1?strTmp.indexOf(','):strTmp.indexOf(')');
				s = strTmp.substring(0,end);
                                if(!s.equals("ON")){
				if(presentFlowerVolumn!=recentFlowerVolumn){
					double a = new Double(s).doubleValue();                 
					angle = a;
					ratoteImage(angle);

					}//else
				
                                //已经吃掉的花会影响角度？
                                else {
                                    double a = new Double(s).doubleValue();
                                    angle=((a-angle)/3+angle)%360;
                                   ratoteImage(angle);
                                    recentFlowerVolumn=presentFlowerVolumn;
                                }
                                }
			}          
             if(strs[i].indexOf('*')==0)
            {	//遇墙
                 time = 0;
               ratoteImage(wall(strs[i]));               
	     }  
             else {//什么都没遇到
                        int n=20;
                        int rad=4;
                        Random ra=new Random();
                        if(stillAlive==2){
                            rad=4;
                        }
                        if(stillAlive==1){//只剩一只，守株待兔！
                            n=30;
                            rad=11;
                        }
                       if(time>n){
				time = 0;
				SpiralDirection = SpiralDirection==1?-1:1;
                                 angle=(angle+SpiralDirection*45)%360;                                
			}
			nextAngle = (angle + SpiralDirection*rad)%360;
                        ratoteImage(nextAngle);
                        setXYs(0);
                        return;
                  }  
		}                      
		setXYs(0);
	}       
 public boolean isCatched(){
	    dead = true;
            stillAlive--;
            killFlag = true;
	    return dead;
	}
    private double specialdd(String strVision) {
        double nextAngle = angle%360;
		Random ra = new Random();
                String[] strs = strVision.split("~");
                int j=0;
                 for(int i=0;i<strs.length;i++){
                       if(strs[i].indexOf('*')!=-1){
                     if(++j>1){
                         return nextAngle;
                     }
                       }
                 }
                for(int i=0;i<strs.length;i++){
                    if(strs[i].indexOf('*')!=-1){     
		if(strs[i].indexOf("E")!=-1){ 
			if(angle > 180){
				nextAngle =(angle+30*(45/(angle-360)))%360;
				//SpiralDirection = -1;
			}
			else{
				nextAngle = (angle+30*(45/angle))%360;
				//SpiralDirection = 1;
			}
		}
		else if(strs[i].indexOf("W")!=-1){
			if(angle > 180){
				nextAngle = (angle+30*(45/(angle-180)))%360;
				//SpiralDirection = 1;
			}
			else{
				nextAngle = (angle+30*(45/(angle-180)))%360;
				//SpiralDirection = -1;
			}
		}
		else if(strs[i].indexOf("N")!=-1){
			if(angle > 270){
                                        
				nextAngle = (angle+30*(45/(angle-270)))%360;
                             //   SpiralDirection = 1;
			}
			else{
				nextAngle = (angle+ 30*(45/(angle-270)))%360;
                             //   SpiralDirection = -1;
			}
		}
		else{
			if(angle > 90){
				nextAngle = (angle+30*(45/(angle-90)))%360;
				//SpiralDirection = 1;
			}
			else{
				nextAngle =(angle+30*(45/(angle-90)))%360;
				//SpiralDirection = -1;
			}
                }
                    }
                    break;
                }
        return nextAngle;
    
    }

    private double specialff(String strVision, double ba, double da) {
       //  Random ra=new Random();
                       String[] strs = strVision.split("~");  
                       
                       double temp=90;
                     for(int i=0;i<strs.length;i++){
                         if(strs[i].indexOf('-')==0){              
				String strTmp = strs[i];				
				int end = strTmp.indexOf(',');
				strTmp = strTmp.substring(end+1);
				end = strTmp.indexOf(',')!=-1?strTmp.indexOf(','):strTmp.indexOf(')');
				String s = strTmp.substring(0,end);
                                if(!s.equals("ON")) {       
					double a = new Double(s).doubleValue(); 
                                        if(Math.abs(a-angle)<10){
					if(Math.abs(a-angle)<temp){
                                            temp=a;
                                                  }
                                        }					
                                }
                        }
                     }
        if(temp<90){             
        angle = temp;
        }
        else{
            angle=da;
        }
        return angle;
    } 
 private double wall( String strs) {          
        
               
                double nextAngle = angle%360;
		Random ra = new Random();
		if(strs.indexOf("E")!=-1){ 
			if(angle > 180){
				nextAngle = 150 + ra.nextInt(60);
				SpiralDirection = 1;
			}
			else{
				nextAngle = 150+ra.nextInt(60);
				SpiralDirection = -1;
			}
		}
		else if(strs.indexOf("W")!=-1){
			if(angle > 180){
				nextAngle = (330+ra.nextInt(60))%360;
				SpiralDirection = -1;
			}
			else{
				nextAngle =(330+ra.nextInt(60))%360;
				SpiralDirection = 1;
			}
		}
		else if(strs.indexOf("N")!=-1){
			if(angle > 270){
				nextAngle =60+ ra.nextInt(60);
                                SpiralDirection = 1;
			}
			else{
				nextAngle = 60+ra.nextInt(60);
                                SpiralDirection = -1;
			}
		}
		else{
			if(angle > 90){
				nextAngle = 240+ra.nextInt(60);
				SpiralDirection = -1;
			}
			else{
				nextAngle =240+ra.nextInt(60);
				SpiralDirection = 1;
			}
                }
                return nextAngle;
 }  
    
    
    
    
    
    
}        
