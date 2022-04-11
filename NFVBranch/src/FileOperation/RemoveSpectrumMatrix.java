package FileOperation;

import java.util.ArrayList;
import java.util.List;

public class RemoveSpectrumMatrix {

    public static void main(String args[]){

        List<String> files = readAllFiles.substrateFileName();

        for (String f : files) {
            FileSystem fObj = new FileSystem(f);
            ArrayList<String> rows = fObj.getFileRows();
            String temp = "";
            for (int i = 0; i<112; i++){
                temp+=rows.get(i)+"\n";
            }

            fObj.writeFile(f,temp);

        }

    }

}
