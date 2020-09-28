package com.sorm.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sorm.bean.ColumnInfo;
import com.sorm.bean.JavaFieldGetSet;
import com.sorm.bean.TableInfo;
import com.sorm.core.DBManager;
import com.sorm.core.MySqlTypeConvertor;
import com.sorm.core.TableContext;
import com.sorm.core.TypeConvertor;

/**
 * ��װ������java�ļ���Դ�룩���õĲ���
 * @author Administrator
 *
 */
public class JavaFileUtils {

	/**
	 * �����ֶ���Ϣ����java������Ϣ���� var username --- private String username; �Լ���Ӧ��set��get����Դ��
	 * @param column  �ֶ���Ϣ
	 * @param convertor ����ת����
	 * @return  java���Ժ�set/get����Դ��
	 */
	public static JavaFieldGetSet createFieldGetSetSRC(ColumnInfo column, TypeConvertor convertor){
		JavaFieldGetSet jfgs = new JavaFieldGetSet();
		String javaFieldType = convertor.databaseType2JavaType(column.getDataType());
		jfgs.setFieldInfo("\tprivate " + javaFieldType + " " + column.getName() + ";\n");
		
		// public int getId(){return id;}
		StringBuilder getSrc = new StringBuilder();
		getSrc.append("\tpublic " + javaFieldType + " get" + StringUtils.firstChar2UpperCase(column.getName() + "(){\n"));
		getSrc.append("\t\treturn " + column.getName() + ";\n");
		getSrc.append("\t}\n");
		jfgs.setGetInfo(getSrc.toString());
		
		// public void setId(int id){this.id = id;}
		StringBuilder setSrc = new StringBuilder();
		setSrc.append("\tpublic void set" + StringUtils.firstChar2UpperCase(column.getName() + "(" + javaFieldType + " " + column.getName() + "){\n"));
		setSrc.append("\t\tthis." + column.getName() + " = " + column.getName() + ";\n");
		setSrc.append("\t}\n");
		jfgs.setSetInfo(setSrc.toString());
		
		return jfgs;
	}
	
	/**
	 * ���ݱ���Ϣ����java���Դ����
	 * @param tableInfo ����Ϣ
	 * @param convertor ��������ת����
	 * @return java���Դ����
	 */
	public static String createJavaSrc(TableInfo tableInfo, TypeConvertor convertor){
		StringBuilder src = new StringBuilder();
		
		// field and setter and getter methods
		Map<String, ColumnInfo> columns = tableInfo.getColumns();
		List<JavaFieldGetSet> javaFields = new ArrayList<JavaFieldGetSet>();
		
		for (ColumnInfo c : columns.values()){
			javaFields.add(createFieldGetSetSRC(c, new MySqlTypeConvertor()));
		}
		
		// package
		src.append("package " + DBManager.getConf().getPoPackage() + ";\n");
		// import
		src.append("import java.sql.*;\n");
		src.append("import java.util.*;\n\n");
		
		// ������
		src.append("public class " + StringUtils.firstChar2UpperCase(tableInfo.getTname() + "{\n\n"));
		// field
		for(JavaFieldGetSet jfgs : javaFields){
			src.append(jfgs.getFieldInfo());
		}
		src.append("\n\n");
		// getter
		for(JavaFieldGetSet jfgs : javaFields){
			src.append(jfgs.getGetInfo());
		}
		// setter
		for(JavaFieldGetSet jfgs : javaFields){
			src.append(jfgs.getSetInfo());
		}
		
		//�����
		src.append("}\n");
	//	System.out.println(src);
		return src.toString();
	}

	public static void createJavaPOFile(TableInfo tableInfo, TypeConvertor convertor){
		String src = createJavaSrc(tableInfo, convertor);
		
		String srcPath = DBManager.getConf().getSrcPath() + "/";
		String packagePath = DBManager.getConf().getPoPackage().replaceAll("\\.", "/");
		File f = new File(srcPath + packagePath);
		
		// ���ָ��Ŀ¼�����ڣ�������û�����
		if(!f.exists()){
			f.mkdirs();
		}
		
		BufferedWriter bw = null;
		try {
			String filePath = f.getAbsolutePath() + "/" + StringUtils.firstChar2UpperCase(tableInfo.getTname()) + ".java";
			bw = new BufferedWriter(new FileWriter(filePath));
			bw.write(src);
			bw.flush();
			
			System.out.println("������" + tableInfo.getTname() + "��Ӧ��java�ࣺ" 
					+ StringUtils.firstChar2UpperCase(tableInfo.getTname()) + ".java");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != bw){
					bw.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
	//	ColumnInfo ci = new ColumnInfo("username", "varchar", 0);
	//	JavaFieldGetSet jf = createFieldGetSetSRC(ci, new MySqlTypeConvertor());
	//	System.out.println(jf.toString());
		Map<String, TableInfo> map = TableContext.tables;
		for(TableInfo t:map.values()){
			createJavaPOFile(t, new MySqlTypeConvertor());
		}
	}
}
