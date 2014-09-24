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
 
 //����midi�ļ����ڴ��д���Sequence�����൱�������
 public void loadMidi(String filename) throws IOException, InvalidMidiDataException{
  
  sequence = MidiSystem.getSequence(this.getClass().getResourceAsStream(filename));
 }
 //���ŷ���
 public void play(){
  if(isPlaying){
   return;
  } 
   try {
        sequencer = MidiSystem.getSequencer();
		sequencer.open();
        //��Sequencer����������ļ����н����������
    sequencer.setSequence(sequence);
    sequencer.addMetaEventListener(this);
     //����ѭ��������-1��ʾһֱѭ��
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