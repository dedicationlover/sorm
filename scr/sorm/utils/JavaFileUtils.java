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
 * 封装了生成java文件（源码）常用的操作
 * @author Administrator
 *
 */
public class JavaFileUtils {

	/**
	 * 根据字段信息生成java属性信息。如 var username --- private String username; 以及相应的set和get方法源码
	 * @param column  字段信息
	 * @param convertor 类型转化器
	 * @return  java属性和set/get方法源码
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
	 * 根据表信息生成java类的源代码
	 * @param tableInfo 表信息
	 * @param convertor 数据类型转化器
	 * @return java类的源代码
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
		
		// 声明类
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
		
		//类结束
		src.append("}\n");
	//	System.out.println(src);
		return src.toString();
	}

	public static void createJavaPOFile(TableInfo tableInfo, TypeConvertor convertor){
		String src = createJavaSrc(tableInfo, convertor);
		
		String srcPath = DBManager.getConf().getSrcPath() + "/";
		String packagePath = DBManager.getConf().getPoPackage().replaceAll("\\.", "/");
		File f = new File(srcPath + packagePath);
		
		// 如果指定目录不存在，则帮助用户建立
		if(!f.exists()){
			f.mkdirs();
		}
		
		BufferedWriter bw = null;
		try {
			String filePath = f.getAbsolutePath() + "/" + StringUtils.firstChar2UpperCase(tableInfo.getTname()) + ".java";
			bw = new BufferedWriter(new FileWriter(filePath));
			bw.write(src);
			bw.flush();
			
			System.out.println("创建了" + tableInfo.getTname() + "对应的java类：" 
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
