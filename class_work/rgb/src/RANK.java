public class RANK {
    public static String[] order(String[] input){
        if(input==null||input.length==0){
            return new String[0];
        }
        int i=0;
        int left=0;
        int right= input.length-1;
        while(i<=right){
            if(input[i].equals("r")){
                String t1=input[i];
                input[i]=input[left];
                input[left]=t1;
                i++;
                left++;
            }else if(input[i].equals("g")){
                i++;
            }else if(input[i].equals("b")){
                String t1=input[i];
                input[i]=input[right];
                input[right]=t1;
                right--;
            }
        }
        return input;
    }

    public static void main(String[] args){
        String[] input = new String[]{"g","r","b","b","r","g","b","r","g","b"};
        order(input);
        for (int i=0;i<input.length;i++){
            System.out.print(input[i]);
        }
    }
}
