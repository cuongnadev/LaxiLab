package org.example.laxilab.Modal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

    public class FileContent {
        private ArrayList<String> nameFile=new ArrayList<>();
        private List<String> content=new ArrayList<>();
        private ArrayList<FileContent> listFile=new ArrayList<>();
        private ArrayList<String> absolutePath=new ArrayList<>();
        public ArrayList<String> getNameFile() {
            return nameFile;
        }

        public void setNameFile(ArrayList<String> nameFile) {
            this.nameFile = nameFile;
        }

        public List<String> getContent() {
            return content;
        }

        public void setContent(List<String> content) {
            this.content = content;
        }

        public ArrayList<FileContent> getListFile() {
            return listFile;
        }

        public void setListFile(ArrayList<FileContent> listFile) {
            this.listFile = listFile;
        }

        public void addFileContent(List<String> content){
            FileContent list= new FileContent();
            list.setContent(content);
        }

        public ArrayList<String> getAbsolutePath() {
            return absolutePath;
        }

        public void setAbsolutePath(ArrayList<String> absolutePath) {
            this.absolutePath = absolutePath;
        }
        public void addAbsolutePath(File file){
            absolutePath.add(file.getAbsolutePath());
        }
        public void addNameFile(File file){
            nameFile.add(file.getName());
        }
    }

