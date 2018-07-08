package com.itheima.handbyhand.bean;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.handbyhand.utils.HashUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class NoteBook {
    ArrayList<Block> list = new ArrayList<Block>();
    private NoteBook(){
        loadFile();
    }
    private static volatile NoteBook instance;
    public static NoteBook getInstance() {
        if (instance == null) {
            synchronized (NoteBook.class) {
                if (instance == null) {
                    instance = new NoteBook();
                }
            }
        }
        return  instance;
    }

    //添加封面
    public void addGenesis(String genesis){
        if (list.size()>0){
            throw new RuntimeException("添加封面失败,已经存在封面");
        }
        String preHash ="0000000000000000000000000000000000000000000000000000000000000000";
        int nonce= mine(genesis+preHash);
        list.add(new Block(list.size()+1,genesis,HashUtils.sha256(genesis +  preHash + nonce ),nonce,preHash));
        save2Disk();
    }

    //添加交易记录
    public void addNote(String note){
        if (list.size()<1){
            throw new RuntimeException("添加记录失败,还没有封面");
        }
        Block block = list.get(list.size() - 1);
        String preHash = block.getHash();
        int nonce= mine(note+preHash);
        list.add(new Block(list.size()+1,note,HashUtils.sha256(note+preHash+nonce),nonce,preHash));
        save2Disk();
    }

    //展示数据,把list集合里面存储的数据返回出去
    public ArrayList<Block> showList(){
        return list;
    }

    //保存数据到本地文件
    public void save2Disk(){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File("a.json"),list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //从本地文件中加载数据
    public void loadFile(){

        try {
            File file = new File("a.json");
            if (file.exists()&&file.length()>0){
                ObjectMapper objectMapper = new ObjectMapper();
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class,Block.class);
                list =objectMapper.readValue(file,javaType);
                System.out.println("本地数据已经加载完成");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //校验数据
    public String check(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <list.size() ; i++) {
            Block block = list.get(i);
            String content = block.getContent();
            int nonce = block.getNonce();
            String hash = block.getHash();
            int id = block.getId();
            String preHash = block.getPreHash();
            //当i=0的时候不需要去验证前一个数据的hash,还一个需要把i=0独立出来愿意是因为需要get(i-1),如果不分开看会导致角标越界,
            //除了i=0之外需要校验当前hash还需要校验preHash
            if (i==0){
                String newHash = HashUtils.sha256(content + nonce + preHash);
                if (!hash.equals(newHash)){
                    sb.append("编号为"+id+"的数据存在问题,请注意查看");
                }
            }else {
                Block preBlock = list.get(i - 1);
                String preBlockHash = preBlock.getHash();
                String newHash = HashUtils.sha256(content + nonce + preHash);
                if (!hash.equals(newHash)){
                    sb.append("编号为"+id+"的数据存在问题,请注意查看");
                }
                if (preHash.equals(preBlockHash)){
                    sb.append("编号为"+id+"的preHash存在问题,请注意查看");
                }
            }

        }
        return sb.toString();
    }


    //挖矿的方法,用来得到特定的格式的hash值,比如以0000开头
    public int mine(String content){
        for (int i = 0; i <Integer.MAX_VALUE ; i++) {
            String newHash = HashUtils.sha256(content + i);
            if (newHash.startsWith("0000")){
                return i;

            }
        }
        throw new RuntimeException("挖矿失败,计算能力不足");
    }

    //比较其他节点的数据和本节点的数据长度,如果大于本节点长度就更新数据
    public void comparaData(ArrayList<Block> newList){
        if (newList.size()>list.size()){
            list = newList;
            save2Disk();
        }
    }
}
