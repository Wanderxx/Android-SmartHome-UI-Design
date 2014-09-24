import java.io.*;
import javax.sound.midi.*;


public class Music implements MetaEventListener, Runnable{
private Sequence sequence = null;
private Sequencer sequencer;
private boolean isPlaying = false;
private volatile Thread thread;
 
 public Music(String midifile){
  try {
   loadMidi(midifile);
  } catch (IOException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (InvalidMidiDataException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
 }
 
 //导入midi文件到内存中传给Sequence对象，相当与编码器
 public void loadMidi(String filename) throws IOException, InvalidMidiDataException{
  
  sequence = MidiSystem.getSequence(this.getClass().getResourceAsStream(filename));
 }
 //播放方法
 public void play(){
  if(isPlaying){
   return;
  } 
   try {
        sequencer = MidiSystem.getSequencer();
		sequencer.open();
        //用Sequencer对象把声音文件序列解码出来播放
    sequencer.setSequence(sequence);
    sequencer.addMetaEventListener(this);
     //设置循环次数，-1表示一直循环
    sequencer.setLoopCount(-1);
    sequencer.setLoopStartPoint(0);
    sequencer.setLoopEndPoint(sequencer.getTickLength());
   } catch (MidiUnavailableException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
   } catch (InvalidMidiDataException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
   }
   if(thread == null){
    thread = new Thread(this);
    thread.start();
   }
 }
 public void stop(){
  if(isPlaying){
   sequencer.stop();
   isPlaying = false;
  }
  if(thread != null){
   thread = null;
  }
 }

 public void meta(MetaMessage meta) {
  if(meta.getType() == 47){
   System.out.println("Sequencer is done playing");
  }
  // TODO Auto-generated method stub
  
 }
 public void run() {
  // TODO Auto-generated method stub
  Thread current = Thread.currentThread();
  while(current == thread && !isPlaying){
   sequencer.start();
   isPlaying = true;
   try {
    thread.sleep(1001);
   } catch (InterruptedException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
   }
  }
 }
} 