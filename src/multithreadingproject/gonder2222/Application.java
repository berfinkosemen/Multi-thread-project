package MultithreadingProject.gonder2222;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class Application {
    int capacityOfMain = 0;
    int ID = 1;     //mainId:0, subthread1Id:1, subthread2Id:2 and the others' id will increase from 3.

    List<Integer> capacitiesOfThreads = new ArrayList<>();
    List<Integer> maxCapacitiesOfThreads = new ArrayList<>();
    List<Thread> subThreads = new ArrayList<>();
    List<Thread> subThreadsDeleted = new ArrayList<>();
    List<Integer> idOfThread = new ArrayList<>();

    private Object lock1 = new Object();
    private Object lock2 = new Object();
    private Object lock3 = new Object();

    static JFrame frame;
    static JProgressBar progressBar1 = new JProgressBar(0, 100);
    static JProgressBar progressBar2 = new JProgressBar(0, 100);
    static JProgressBar progressBar3 = new JProgressBar(0, 100);
    static JProgressBar progressBar4 = new JProgressBar(0, 100);
    static List<JProgressBar> progressBars = new ArrayList<>();
    static JLabel jlabel = new JLabel();
    static JLabel jlabel_counter = new JLabel();
    static JLabel jlabel_counter_value = new JLabel();
    static JLabel jlabel_main = new JLabel();
    static JLabel jlabel_sub1 = new JLabel();
    static JLabel jlabel_sub2 = new JLabel();
    static JLabel jlabel_main_percent = new JLabel();
    static JLabel jlabel_sub1_percent = new JLabel();
    static JLabel jlabel_sub2_percent = new JLabel();
    static List<JLabel> jlabels = new ArrayList<>();
    static List<JLabel> jlabels_percent = new ArrayList<>();
    static List<Integer> control = new ArrayList<Integer>();

    public Application() {

        initialize();

    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        jlabel.setText("SUNUCU TAKIP");
        jlabel.setBounds(250, 20, 100, 20);
        frame.add(jlabel);
        jlabel_counter.setText("SUNUCU SAYISI : ");
        jlabel_counter.setBounds(40, 50, 100, 20);
        jlabel_counter_value.setText("3");
        jlabel_counter_value.setBounds(140, 50, 100, 20);
        frame.add(jlabel_counter_value);
        frame.add(jlabel_counter);

        progressBar1.setStringPainted(true);
        progressBar1.setForeground(Color.red);
        progressBar1.setBounds(250, 100, 259, 14);
        frame.getContentPane().add(progressBar1);

        jlabel_main.setText("ANA SUNUCU : ");
        jlabel_main.setBounds(40, 100, 100, 20);
        jlabel_main_percent.setText("%0");
        jlabel_main_percent.setBounds(150, 100, 100, 20);
        frame.add(jlabel_main_percent);
        frame.add(jlabel_main);

        progressBar2.setStringPainted(true);
        progressBar2.setForeground(Color.BLUE);
        progressBar2.setBounds(250, 130, 259, 14);
        frame.getContentPane().add(progressBar2);

        jlabel_sub1.setText("ALT SUNUCU 1 : ");
        jlabel_sub1.setBounds(40, 130, 100, 20);
        jlabel_sub1_percent.setText("%0");
        jlabel_sub1_percent.setBounds(150, 130, 100, 20);
        frame.add(jlabel_sub1_percent);
        frame.add(jlabel_sub1);

        progressBar3.setStringPainted(true);
        progressBar3.setForeground(Color.BLUE);
        progressBar3.setBounds(250, 160, 259, 14);
        frame.getContentPane().add(progressBar3);

        jlabel_sub2.setText("ALT SUNUCU 2 : ");
        jlabel_sub2.setBounds(40, 160, 100, 20);
        jlabel_sub2_percent.setText("%0");
        jlabel_sub2_percent.setBounds(150, 160, 100, 20);
        frame.add(jlabel_sub2_percent);
        frame.add(jlabel_sub2);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    Application app = new Application();
                    app.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        Application obj = new Application();

        for (int i = 0; i < 50; i++) {
            obj.capacitiesOfThreads.add(0);
            obj.maxCapacitiesOfThreads.add(100);
            obj.idOfThread.add(i);
        }
        obj.maxCapacitiesOfThreads.set(0, 150);

        obj.subThreads.add(new Thread());
        obj.subThreads.add(new Thread());
        obj.subThreads.add(new Thread());

        Thread mainThread = new Thread(() -> {
            mainThread(obj);

        });

        Thread subThread1 = new Thread(() -> {
            subThread(obj, 1, 0);
        });

        Thread subThread2 = new Thread(() -> {
            subThread(obj, 2, 0);
        });

        Thread subThreadGUI = new Thread(() -> {
            subThreadGUI(obj);
        });

        Thread subThreadCreator = new Thread(() -> {
            subThreadCreator(obj);
        });

        obj.startThread(mainThread);
        obj.startThread(subThread1);
        obj.startThread(subThread2);
        obj.startThread(subThreadGUI);
        obj.startThread(subThreadCreator);

        obj.joinThread(mainThread);
        obj.joinThread(subThread1);
        obj.joinThread(subThread2);
        obj.joinThread(subThreadGUI);
        obj.joinThread(subThreadCreator);
    }


    public static void mainThread(Application obj) {
        int id = 0;
        int maxCapacity = 100;
        obj.maxCapacitiesOfThreads.set(id, maxCapacity);

        while (true) {
            obj.sleepThread(500); //500ms aralıkla

            int request = obj.randomNumberCreator(100); //[0-100] arası istek kabul eder
            System.out.println(String.format("%-30s " + request, "mainThread - istek alindi: "));

            if (obj.checkIfTheFirstNumberIsGreater(request, maxCapacity) ||
                    obj.checkIfTheFirstNumberIsGreater((obj.capacityOfMain + request), maxCapacity)) {
                synchronized (obj.lock1) {
                    request = maxCapacity;
                    obj.capacityOfMain = request;
                }
            } else if (obj.checkIfTheNumberIsLessThanCapacity((obj.capacityOfMain + request), maxCapacity)) {
                synchronized (obj.lock1) {
                    obj.capacityOfMain += request;
                }
            } else {
                //do nothing
            }

            System.out.println(String.format("%-30s" + obj.capacityOfMain, "mainThread - capacityOfMain: "));

            synchronized (obj.lock1) {
                changeTheCapacityValue(obj, id, obj.capacityOfMain);
            }

            if (obj.checkIfTheFirstNumberIsGreater(obj.capacityOfMain, 0)) {     //istek oldugu surece

                obj.sleepThread(200); //200 msde bir

                int returningRequests = obj.randomNumberCreator(50);// [0-50] arası istege ...*/
                System.out.println(String.format("%-30s" + returningRequests, "mainThread - geridonus: "));

                if (obj.checkIfTheFirstNumberIsGreater(returningRequests, obj.capacityOfMain)) {
                    synchronized (obj.lock1) {
                        returningRequests = obj.capacityOfMain;
                    }
                }
                synchronized (obj.lock1) {
                    obj.capacityOfMain = obj.capacityOfMain - returningRequests;                      // ... geri donus yapar.
                    System.out.println(String.format("%-30s" + obj.capacityOfMain + "\n", "mainThread - capacityOfMain: "));

                    changeTheCapacityValue(obj, id, obj.capacityOfMain);
                }
                if (obj.checkIfTheFirstNumberIsGreater(0, obj.capacityOfMain)) {
                    synchronized (obj.lock1) {
                        obj.capacityOfMain = 0;
                    }
                }

                synchronized (obj.lock1) {
                    changeTheCapacityValue(obj, id, obj.capacityOfMain);
                }
            }
        }
    }


    private static void subThread(Application obj, int id, int capacity) {
        obj.ID++;
        System.out.println("came subthread id: " + obj.idOfThread.get(id));
        int maxCapacity = 100;
        obj.maxCapacitiesOfThreads.add(obj.idOfThread.get(id), maxCapacity);


        while (true) {
            obj.sleepThread(500);// 500 msde bir ...
            synchronized (obj.lock2) {
                if (obj.checkIfTheFirstNumberIsGreater(obj.capacityOfMain, 0)) {
                    int requestsFromMain = obj.randomNumberCreator(50); // ... [0-50] arasi istek kuyruktan alir*/

                    if (obj.checkIfTheFirstNumberIsGreater(requestsFromMain, obj.capacityOfMain)) {
                        synchronized (obj.lock1) {
                            requestsFromMain = obj.capacityOfMain;
                            obj.capacityOfMain = 0;
                        }
                    } else {
                        synchronized (obj.lock1) {
                            obj.capacityOfMain = obj.capacityOfMain - requestsFromMain;
                        }
                    }

                    System.out.println(String.format("%-30s" + requestsFromMain, "SubThread" + obj.idOfThread.get(id) + " - istek alindi: "));

                    changeTheCapacityValue(obj, 0, obj.capacityOfMain);
                    capacity = obj.capacitiesOfThreads.get(obj.idOfThread.get(id));

                    if (obj.checkIfTheFirstNumberIsGreater(requestsFromMain, maxCapacity)
                            || obj.checkIfTheFirstNumberIsGreater((obj.capacitiesOfThreads.get(obj.idOfThread.get(id)) + requestsFromMain), maxCapacity)) {
                        synchronized (obj.lock1) {
                            requestsFromMain = maxCapacity;
                            obj.capacitiesOfThreads.set(obj.idOfThread.get(id), requestsFromMain);
                        }
                    } else if (obj.checkIfTheNumberIsLessThanCapacity((obj.capacitiesOfThreads.get(obj.idOfThread.get(id)) + requestsFromMain), maxCapacity)) {
                        synchronized (obj.lock1) {
                            obj.capacitiesOfThreads.set(obj.idOfThread.get(id), obj.capacitiesOfThreads.get(obj.idOfThread.get(id)) + requestsFromMain);
                            capacity += requestsFromMain;
                        }
                    } else {
                        //do nothing
                    }

                    synchronized (obj.lock1) {
                        System.out.println(String.format("%-30s" + obj.capacitiesOfThreads.get(obj.idOfThread.get(id)), "SubThread" + obj.idOfThread.get(id) + " - sumIstek: "));
                        changeTheCapacityValue(obj, obj.idOfThread.get(id), capacity);
                        obj.capacitiesOfThreads.set(obj.idOfThread.get(id), obj.capacitiesOfThreads.get(obj.idOfThread.get(id)));
                        capacity = obj.capacitiesOfThreads.get(obj.idOfThread.get(id));

                        obj.sleepThread(300);//300 msde bir
                    }

                    int returningRequests = obj.randomNumberCreator(50);//[0 50] arası istege ...*/
                    System.out.println(String.format("%-30s" + returningRequests, "id:" + id + "SubThread" + obj.idOfThread.get(id) + " - geridonus: "));

                    if (obj.checkIfTheFirstNumberIsGreater(returningRequests, obj.capacitiesOfThreads.get(obj.idOfThread.get(id)))) {
                        synchronized (obj.lock1) {
                            returningRequests = obj.capacitiesOfThreads.get(obj.idOfThread.get(id));
                        }
                    }
                    synchronized (obj.lock1) {
                        obj.capacitiesOfThreads.set(obj.idOfThread.get(id), obj.capacitiesOfThreads.get(obj.idOfThread.get(id)) - returningRequests);
                        capacity = capacity - returningRequests;
                        System.out.println(String.format("%-30s" + obj.capacitiesOfThreads.get(obj.idOfThread.get(id)) + "\n", "id:" + id + "SubThread" + obj.idOfThread.get(id) + " - sumIstek: "));

                        changeTheCapacityValue(obj, obj.idOfThread.get(id), capacity);
                        obj.capacitiesOfThreads.set(obj.idOfThread.get(id), obj.capacitiesOfThreads.get(obj.idOfThread.get(id)));
                        capacity = obj.capacitiesOfThreads.get(obj.idOfThread.get(id));

                    }

                    if (obj.checkIfTheFirstNumberIsGreater(0, obj.capacityOfMain)) {
                        synchronized (obj.lock1) {
                            obj.capacityOfMain = 0;
                        }
                    }
                    synchronized (obj.lock1) {
                        changeTheCapacityValue(obj, 0, obj.capacityOfMain);

                        capacity = obj.capacitiesOfThreads.get(obj.idOfThread.get(id));
                        obj.capacitiesOfThreads.set(obj.idOfThread.get(id), obj.capacitiesOfThreads.get(obj.idOfThread.get(id)));

                    }

                    if (obj.checkIfTheFirstNumberIsGreater(0, capacity)) {
                        synchronized (obj.lock1) {
                            capacity = 0;
                            obj.capacitiesOfThreads.set(obj.idOfThread.get(id), 0);

                        }
                    }
                    synchronized (obj.lock1) {
                        changeTheCapacityValue(obj, obj.idOfThread.get(id), capacity);
                        capacity = obj.capacitiesOfThreads.get(obj.idOfThread.get(id));
                        obj.capacitiesOfThreads.set(obj.idOfThread.get(id), obj.capacitiesOfThreads.get(obj.idOfThread.get(id)));
                    }

                    synchronized (obj.lock1) {
                        System.out.println(String.format("%-30s" + obj.capacityOfMain + "\n", "SubThread" + obj.idOfThread.get(id) + " - capacityOfMain: "));
                    }
                }
            }
        }
    }


    private static void subThreadGUI(Application obj) {
        while (true) {
            //synchronized(obj.lock2){
            update_swing(obj);
            //findThreadsWithCapacityLessThan0_2(obj);
            //}
        }
    }

    public static void subThreadCreator(Application obj) {
        while (true) {
            Thread subthread = null;

            List<Integer> indexes = findThreadsWithCapacityMoreThan70(obj);
            List<Integer> indexesForZero = findThreadsWithCapacityLessThan0(obj);

            for (int i = 1; i < obj.subThreads.size(); i++) {
                if (indexes.get(i) != -1) {
                    System.out.println("i: " + i + "value: " + indexes.get(i));
                }
                if (indexesForZero.get(i) != -1) {
                    System.out.println("i: " + i + "value: " + indexesForZero.get(i));
                }
                if (indexes.get(i) != -1) {
                    int id = obj.subThreads.size();
                    int newCapacity = obj.capacitiesOfThreads.get(i) / 2;

                    if(obj.capacitiesOfThreads.get(i) % 2 == 1) {
                        obj.capacitiesOfThreads.set(i, (newCapacity+1));
                    } else {
                        obj.capacitiesOfThreads.set(i, newCapacity);
                    }

                    subthread = new Thread(() -> {
                        subThread(obj, obj.ID, newCapacity);
                    });

                    boolean x = obj.subThreads.add(subthread);
                    obj.startThread(obj.subThreads.get(id));

                    indexes.add(-1);
                    indexesForZero.add(-1);
                    obj.capacitiesOfThreads.set(id, newCapacity);

                    System.out.println("ID: " + id);

                    for (int j = 0; j < indexes.size(); j++)
                        indexes.set(j, -1);

                    System.out.println("Yeni bir thread olusturuldu. Kapasite: " + newCapacity + "id:" + id);
                    System.out.println(" cr bf obj.subThreads.size(): " + obj.subThreads.size() + "id: " + id);
                }

                if (indexesForZero.get(i) != -1) {

                    int id = obj.subThreads.size();
                    System.out.println("id: " + (id));

                    indexesForZero.remove(i);
                    indexes.remove(i);

                    boolean x = obj.deleteThread(obj, i);

                    int start = obj.idOfThread.size();
                    boolean flag = false;
                    int k = i;
                    int o = k;
                    while (flag == false && k != obj.idOfThread.size()) {
                        while (flag == false && o != obj.idOfThread.size()) {
                            if (k < obj.idOfThread.get(o)) {
                                start = o;
                                flag = true;
                                break;
                            }
                            o++;
                        }
                        k++;
                    }


                    for (k = start; k < obj.idOfThread.size(); k++) {
                        obj.idOfThread.set(k, obj.idOfThread.get(k) - 1);
                    }

                    System.out.print("idler: ");
                    for (k = 0; k < obj.subThreads.size() + 10; k++) {
                        System.out.print(obj.idOfThread.get(k) + " ");
                    }
                    System.out.println();
                    System.out.println();

                    id = obj.subThreads.size();
                    System.out.println("dl bf obj.subThreads.size(): " + obj.subThreads.size() + "id: " + id);

                    for (int j = 0; j < indexesForZero.size(); j++)
                        indexesForZero.set(j, -1);

                    obj.subThreadsDeleted.add(obj.subThreads.get(i));
                    obj.subThreads.remove(i);

                    for (int p = 0; p < obj.subThreadsDeleted.size(); p++) {
                        if (obj.subThreadsDeleted.get(p).isInterrupted() == false) {
                            obj.subThreadsDeleted.get(p).stop();
                        }
                    }
                }
            }
        }

    }


    public boolean deleteThread(Application obj, int id) {
        obj.capacitiesOfThreads.remove(id);
        obj.maxCapacitiesOfThreads.remove(id);

        System.out.println("thread silindi : " + id);

        return true;
    }


    public void startThread(Thread thread) {
        thread.start();
    }


    public void joinThread(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void sleepThread(int millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public int randomNumberCreator(int range) {
        Random random = new Random();
        return (random.nextInt(range)+1);
    }


    public boolean checkIfTheNumberIsLessThanCapacity(int number, int capacity) {  //if number smaller than capacity return true, else false
        if (number <= capacity) {
            return true;
        } else {
            return false;
        }
    }


    public boolean checkIfTheFirstNumberIsGreater(int number1, int number2) { //Istek varsa yani 0dan kucukesit degilse true doner
        if (number1 > number2) {      //1. sayı 2.den buyuk esitse true
            return true;
        } else {
            return false;
        }
    }


    public static void changeTheCapacityValue(Application obj, int id, int capacity) {
        synchronized (obj.lock1) {
            if (id == 0) {
                obj.capacitiesOfThreads.set(id, capacity);
            }

            System.out.print("Capacities: ");
            for (int i = 0; i < obj.subThreads.size(); i++)
                System.out.print(obj.capacitiesOfThreads.get(i) + " ");
            System.out.println("id: " + id + "\n");
        }
    }


    public boolean isCapacityOver70Percent(int maxCapacity, int capacity) {
        float measure = ((float) ((float) capacity / maxCapacity)) * 100;

        if (measure >= 70) {
            return true;
        } else {
            return false;
        }
    }


    public static List<Integer> findThreadsWithCapacityMoreThan70(Application obj) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < obj.subThreads.size(); i++) {
            indexes.add(-1);
        }
        for (int i = 1; i < obj.subThreads.size(); i++) {
            if (obj.isCapacityOver70Percent(obj.maxCapacitiesOfThreads.get(i), obj.capacitiesOfThreads.get(i))) {
                indexes.set(i, i);

                System.out.print(indexes.get(i));
                System.out.println();
                System.out.println();
            }
        }
        return indexes;
    }


    public boolean isCapacity0Percent(int capacity) {
        if (capacity == 0) {
            return true;
        } else {
            return false;
        }
    }


    public static List<Integer> findThreadsWithCapacityLessThan0(Application obj) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < obj.subThreads.size(); i++) {
            indexes.add(-1);
        }
        for (int i = 3; i < obj.subThreads.size(); i++) {
            if (obj.isCapacity0Percent(obj.capacitiesOfThreads.get(i))) {
                indexes.set(i, i);

                System.out.print("indexesf0 : ");
                System.out.print(indexes.get(i));
                System.out.println();
                System.out.println();
                System.out.println("obj.subThreads.size(): " + obj.subThreads.size());
            }
        }
        return indexes;
    }




public void isCapacityOver70Percent_2(int maxCapacity, int capacity, int i, int size) {
        
        float measure = ((float) ((float) capacity / maxCapacity)) * 100;
        int measure1 = (int) measure;
        int j, k, t;
        
        jlabel_counter_value.setText(" " + (size));
        
        if(i == 0){
            progressBar1.setValue(measure1);
            progressBar1.repaint();
            progressBar1.setString(Integer.toString(measure1)+"%"); 
            jlabel_main_percent.setText("%" + measure1);
        }
        else if(i == 1){                
            progressBar2.setValue(measure1);
            progressBar2.repaint();
            progressBar2.setString(Integer.toString(measure1)+"%"); 
            jlabel_sub1_percent.setText("%" + measure1);
        }else if(i == 2){           
            progressBar3.setValue(measure1);
            progressBar3.repaint();
            progressBar3.setString(Integer.toString(measure1)+"%"); 
            jlabel_sub2_percent.setText("%" + measure1);
        }else if(i > 2){

            
            for(j =0 ;j<size-3; j++){
                                    
                progressBars.add(new JProgressBar(0, 100));
                progressBars.get(j).setStringPainted(true);
                progressBars.get(j).setForeground(Color.GREEN);
                progressBars.get(j).setBounds(250, 100 + 30 * (j + 3), 259, 14);
                frame.getContentPane().add(progressBars.get(j));   
                
               /* jlabels.add(new JLabel());
                String str = "ALT SUNUCU " + i + " : ";
                jlabels.get(j).setText(str);
                jlabels.get(j).setBounds(40,100+30*(j+3) , 100, 20);
                frame.add(jlabels.get(j));*/
                
                /*jlabels_percent.add(new JLabel());
                jlabels_percent.get(j).setText(" ");
                jlabels_percent.get(j).setBounds(150,100+30*(j+3) , 100, 20);
                frame.getContentPane().add(jlabels_percent.get(j));*/
            }
            
            for(k = 0;k<size-3;k++){
                if(k == i-3){
                    progressBars.get(i-3).setValue(measure1);
                    progressBars.get(i-3).repaint();
                    progressBars.get(i-3).setString(Integer.toString(measure1) + "%");
                    
                    jlabels.add(new JLabel());
                    jlabels.get(k).setText(" ");
                    jlabels.get(k).setBounds(40,100+30*(k+3) , 100, 20);
                    frame.getContentPane().add(jlabels.get(k));
                    jlabels.get(k).setText("ALT SUNUCU " + i + " : ");                    
                    
                    jlabels_percent.add(new JLabel());
                    jlabels_percent.get(k).setText(" ");
                    jlabels_percent.get(k).setBounds(150,100+30*(k+3) , 100, 20);
                    frame.getContentPane().add(jlabels_percent.get(k));
                    jlabels_percent.get(i-3).setText("%" + measure1);
                }    
            }
        }
        
        if(control.size() != 0){
            if(control.get(control.size()-1) > size){
                for(k = size-3; k < control.get(control.size()-1)-3;k++){                       
                    frame.remove(progressBars.get(k));
                    frame.remove(jlabels_percent.get(k));
                    frame.remove(jlabels.get(k));
                    frame.getContentPane().remove(progressBars.get(k));
                    frame.getContentPane().remove(jlabels_percent.get(k));
                    frame.getContentPane().remove(jlabels.get(k));
                    frame.repaint();
                }   
            }       
        }
        
        if(control.size() > 5){
            for(t=0;t<control.size()-2;t++){
                control.remove(t);
            }
        }
        
        control.add(size);
    }  

    public static void update_swing(Application obj) {
        for (int i = 0; i < obj.subThreads.size(); i++) {
            obj.isCapacityOver70Percent_2(obj.maxCapacitiesOfThreads.get(i), obj.capacitiesOfThreads.get(i), i, obj.subThreads.size());
            
        }
    }
}
