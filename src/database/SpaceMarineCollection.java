package database;
import javax.management.ObjectName;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class SpaceMarineCollection {

    private final Map<Long, SpaceMarine> linkedHashMap = new LinkedHashMap<>();

    public String info() {
        return ("Type of collection is " + linkedHashMap.getClass().toString() + ". Size of collection is " + linkedHashMap.size());
    }

    public String insert(String stringKey, SpaceMarine spaceMarine) {
        long key;
        try {
            key = Long.parseLong(stringKey);
            if (!linkedHashMap.containsKey(key)) {
                linkedHashMap.put(key, spaceMarine);
                return("Element is inserted");
            } else return("Collection already has element with this ID");
        } catch (NumberFormatException e) {
            return("Incorrect input! Try again.");
        }
    }

    public String remove(String stringKey){
        long key;
        try {
            key = Long.parseLong(stringKey);
            linkedHashMap.remove(key);
            return("Element was removed");
        } catch (NumberFormatException e) {
            return("Incorrect input! Try again.");
        }
    }

    public String update(String stringID, SpaceMarine spaceMarine){
        long id;
        long key = 0;
        boolean check = false;
        try {
            id = Long.parseLong(stringID);
            Map<Long, SpaceMarine> map = linkedHashMap;
            for (Map.Entry<Long, SpaceMarine> entry : map.entrySet()) {
                if (Objects.equals(id, map.get(entry.getKey()).getId())) {
                    key = entry.getKey();
                    check = true;
                }
            }

            if (check) {
                linkedHashMap.replace(key, spaceMarine);
                return("Element is updated");
            }else {
                return("There is no element with ID: " + stringID);
            }
        }catch(NumberFormatException e){
            return("Incorrect input! Try again.");
        }
    }

    public String clear(){
        linkedHashMap.clear();
        return("Collection is empty");
    }

    public String show(){
        Map map = linkedHashMap;
        List<Map.Entry<Long, SpaceMarine>> list = new LinkedList<Map.Entry<Long, SpaceMarine>>(map.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<Long, SpaceMarine>>()
        {
            public int compare(Map.Entry<Long, SpaceMarine> o1,
                               Map.Entry<Long, SpaceMarine> o2)
            {
                return o1.getValue().getName().compareTo(o2.getValue().getName());
            }
        });

        Map<Long, SpaceMarine> sortedMap = new LinkedHashMap<Long, SpaceMarine>();
        for (Map.Entry<Long, SpaceMarine> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        String line = "";
        Set set = sortedMap.entrySet();
        for (Object element: set) {
            Map.Entry mapEntry = (Map.Entry) element;
            line += (mapEntry.getValue().toString()) + "\n";
        }
        return "LinkedHashMap initial content: \n" + line;
    }

    public String remove_greater_key(String stringKey){
        long key;
        try {
            key = Long.parseLong(stringKey);
            Set set = linkedHashMap.entrySet();
            for (Object element : set) {
                Map.Entry mapEntry = (Map.Entry) element;
                if ((Long)mapEntry.getKey() > key){
                    linkedHashMap.remove(key);
                }
            }
            return("Elements with key more than " + key + " were deleted.");
        }catch (NumberFormatException e){
            return("Incorrect input! Try again.");
        }
    }

    public String remove_lower_key(String stringKey){
        long key;
        try {
            key = Long.parseLong(stringKey);
            Set set = linkedHashMap.entrySet();
            for (Object element : set) {
                Map.Entry mapEntry = (Map.Entry) element;
                if ((Long)mapEntry.getKey() < key){
                    linkedHashMap.remove(key);
                }
            }
            return ("Elements with key more than " + key + " were deleted.");
        }catch (NumberFormatException e){
            return ("Incorrect input! Try again.");
        }
    }

    public String remove_any_by_health(String stringHealth){
        Double health;
        boolean check = false;
        try {
            health = Double.parseDouble(stringHealth);
            Set set = linkedHashMap.entrySet();
            String line = "";
            for (Object element : set) {
                Map.Entry mapEntry = (Map.Entry) element;
                if ( linkedHashMap.get(mapEntry.getKey()).getHealth() == health) {
                    line += mapEntry.getKey();
                    linkedHashMap.remove(mapEntry.getKey());
                    check = true;
                    break;
                }
            }
            if (!check) {
                return("There is no element with health " + health);
            } else return "Elements with key " + line + " was deleted.";
        }catch (NumberFormatException e){
            return("Incorrect input! Try again.");
        }
    }

    public String replace_if_lowe(String stringKey, SpaceMarine spaceMarine){
        long key;
        try {
            key = Long.parseLong(stringKey);
            if (spaceMarine.getMarinesCount() < linkedHashMap.get(key).getMarinesCount()){
                linkedHashMap.replace(key, spaceMarine);
                return("Element was replaced");
            }else {
                return("Element wasn't replaced");
            }
        }catch (NumberFormatException e){
            return("Incorrect input! Try again.");
        }
    }

    public String group_counting_by_health(){
        Map<Double, Integer> map = new TreeMap<>();
        double health;
        Integer count;
        Set set = linkedHashMap.entrySet();
        for (Object element : set) {
            Map.Entry mapEntry = (Map.Entry) element;
            health = linkedHashMap.get(mapEntry.getKey()).getHealth();
            count = map.get(health);
            if (count == null) {
                map.put(health, 1);
            } else {
                map.replace(health, ++count);
            }
        }
        return (map.toString());
    }

    public String count_less_than_health(String stringHealth){
        Double health;
        int check = 0;
        try {
            health = Double.parseDouble(stringHealth);
            Set set = linkedHashMap.entrySet();
            for (Object element : set) {
                Map.Entry mapEntry = (Map.Entry) element;
                if ( linkedHashMap.get(mapEntry.getKey()).getHealth() < health) {
                    check++;
                }
            }
            return ("There is " + check +  " elements with health less than " + health);
        }catch (NumberFormatException e){
            return ("Incorrect input! Try again.");
        }
    }



    public void writeToFIle(String nameFile){
        File f = new File(nameFile);
        if(f.exists()) {
            System.out.println("File exists. File will reset. Are you sure you want do it? \n Enter '1' if you want or something else if you don't want.");
            Scanner Check = new Scanner(System.in);
            String check = Check.nextLine();
            int save;
            try{
                save = Integer.parseInt(check);
            }catch(NumberFormatException e){
                save = 0;
            }
            if (save == 1) {
                try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(nameFile))) {
                    Map<Long, SpaceMarine> map = linkedHashMap;
                    Iterator it = map.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        String csv = "" + pair.getKey() + "," + linkedHashMap.get(pair.getKey()).toCsv() + "\n";
                        byte[] buffer = csv.getBytes("Windows-1251");
                        bos.write(buffer, 0, buffer.length);

                    }
                    System.out.println("Collection is saved in " + nameFile);
                } catch(IOException e){
                    System.out.println(e.getMessage());
                }
            }else {
                System.out.println("Collection didn't save");
            }
        }
    }
}
