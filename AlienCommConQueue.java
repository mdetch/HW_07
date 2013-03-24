import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AlienCommConQueue {
	static long start;
	static long end;
	public static void main(String[] args) throws InterruptedException{
		BlockingQueue<ArrayList<AlienLib.Garble>> signals = new LinkedBlockingQueue<ArrayList<AlienLib.Garble>>(10);
		
		Thread getSignal = new GetSignal(signals);
		Thread processSignal = new ProcessSignal(signals);
		
		start = System.nanoTime();
		getSignal.start();
		processSignal.start();
	}
	
	public static class GetSignal extends Thread {
		private BlockingQueue<ArrayList<AlienLib.Garble>> signals;
		
		public GetSignal(BlockingQueue<ArrayList<AlienLib.Garble>> signals){
			this.signals = signals;
			System.out.println("Initiaing communication system...");
		}
		
		public void run(){
			while(AlienLib.isAlienTransmitting()){
				try {
					signals.add(AlienLib.getAlienSignals());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static class ProcessSignal extends Thread {
		private BlockingQueue<ArrayList<AlienLib.Garble>> signals;
		private int i;
		
		public ProcessSignal(BlockingQueue<ArrayList<AlienLib.Garble>> signals){
			this.signals = signals;
			i=0;
		}
		
		public void run(){
			while(true){
				try {
					trasmittingSignal(processingSignal(signals.take()),i++);
					if(i>6)	break;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					break;
				}
			}
			end = System.nanoTime();
            System.out.println("Done processing alien's data.");
            System.out.println("Time taken: "+(end-start)/1.0e9);
		}
		
		public ArrayList<AlienLib.Data> processingSignal(ArrayList<AlienLib.Garble> x){
			ArrayList<AlienLib.Data> data = new ArrayList<AlienLib.Data>();
			for(AlienLib.Garble g: x){
				try {
					data.add(AlienLib.processAlienSignal(g));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return data;
		}
		
		public void trasmittingSignal(ArrayList<AlienLib.Data> x,int i){
			try{
				AlienLib.transmitToPrimeMinisterOffice(x);
			}catch(Exception e){
				e.printStackTrace();
			}
			System.out.println("Pass: "+ i++ +" with "+ x.size()+" signals.");
		}
	}
}
