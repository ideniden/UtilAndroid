package com.luoj.android.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类相关的工具类
 * 
 * @author <a href="mailto:ohergal@gmail.com">ohergal</a>
 * 
 */
public class ClassUtil {

	public static void main(String[] args) throws Exception {
//		List<Class> classes = ClassUtil.getAllClassByInterface(Class
//				.forName("com.threeti.dao.base.IGenericDao"));
		List<Class<?>> classes =getClasses("");
		for (Class<?> clas : classes) {
			System.out.println(clas.getName());
		}
	}

	/**
	 * 取得某个接口下所有实现这个接口的类
	 * */
	public static List<Class<?>> getAllClassByInterface(Class<?> c) {
		List<Class<?>> returnClassList = null;

		if (c.isInterface()) {
			// 获取当前的包名
			String packageName = c.getPackage().getName();
			// 获取当前包下以及子包下所以的类
			List<Class<?>> allClass = getClasses(packageName);
			if (allClass != null) {
				returnClassList = new ArrayList<Class<?>>();
				for (Class<?> classes : allClass) {
					// 判断是否是同一个接口
					if (c.isAssignableFrom(classes)) {
						// 本身不加入进去
						if (!c.equals(classes)) {
							returnClassList.add(classes);
						}
					}
				}
			}
		}

		return returnClassList;
	}

	/*
	 * 取得某一类所在包的所有类名 不含迭代
	 */
	public static String[] getPackageAllClassName(String classLocation,
			String packageName) {
		// 将packageName分解
		String[] packagePathSplit = packageName.split("[.]");
		String realClassLocation = classLocation;
		int packageLength = packagePathSplit.length;
		for (int i = 0; i < packageLength; i++) {
			realClassLocation = realClassLocation + File.separator
					+ packagePathSplit[i];
		}
		File packeageDir = new File(realClassLocation);
		if (packeageDir.isDirectory()) {
			return packeageDir.list();
		}
		return null;
	}

	/**
	 * 从包package中获取所有的Class
	 * 
	 * @param packageName
	 * @return
	 */
	public static List<Class<?>> getClasses(String packageName) {

		// 第一个class类的集合
		List<Class<?>> classes = new ArrayList<Class<?>>();
		// 是否循环迭代
		boolean recursive = true;
		// 获取包的名字 并进行替换
		String packageDirName = packageName.replace('.', '/');
		// 定义一个枚举的集合 并进行循环来处理这个目录下的things
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader()
					.getResources(packageDirName);
			// 循环迭代下去
			while (dirs.hasMoreElements()) {
				// 获取下一个元素
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if ("file".equals(protocol)) {
					// 获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					// 以文件的方式扫描整个包下的文件 并添加到集合中
					findAndAddClassesInPackageByFile(packageName, filePath,
							recursive, classes);
				} else if ("jar".equals(protocol)) {
					// 如果是jar包文件
					// 定义一个JarFile
					JarFile jar;
					try {
						// 获取jar
						jar = ((JarURLConnection) url.openConnection())
								.getJarFile();
						// 从此jar包 得到一个枚举类
						Enumeration<JarEntry> entries = jar.entries();
						// 同样的进行循环迭代
						while (entries.hasMoreElements()) {
							// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
							JarEntry entry = entries.nextElement();
							String name = entry.getName();
							// 如果是以/开头的
							if (name.charAt(0) == '/') {
								// 获取后面的字符串
								name = name.substring(1);
							}
							// 如果前半部分和定义的包名相同
							if (name.startsWith(packageDirName)) {
								int idx = name.lastIndexOf('/');
								// 如果以"/"结尾 是一个包
								if (idx != -1) {
									// 获取包名 把"/"替换成"."
									packageName = name.substring(0, idx)
											.replace('/', '.');
								}
								// 如果可以迭代下去 并且是一个包
								if ((idx != -1) || recursive) {
									// 如果是一个.class文件 而且不是目录
									if (name.endsWith(".class")
											&& !entry.isDirectory()) {
										// 去掉后面的".class" 获取真正的类名
										String className = name.substring(
												packageName.length() + 1,
												name.length() - 6);
										try {
											// 添加到classes
											classes.add(Class
													.forName(packageName + '.'
															+ className));
										} catch (ClassNotFoundException e) {
											e.printStackTrace();
										}
									}
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return classes;
	}

	/**
	 * 以文件的形式来获取包下的所有Class
	 * 
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	public static void findAndAddClassesInPackageByFile(String packageName,
			String packagePath, final boolean recursive, List<Class<?>> classes) {
		// 获取此包的目录 建立一个File
		File dir = new File(packagePath);
		// 如果不存在或者 也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		// 如果存在 就获取包下的所有文件 包括目录
		File[] dirfiles = dir.listFiles(new FileFilter() {
			// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
			public boolean accept(File file) {
				return (recursive && file.isDirectory())
						|| (file.getName().endsWith(".class"));
			}
		});
		// 循环所有文件
		for (File file : dirfiles) {
			// 如果是目录 则继续扫描
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(
						packageName + "." + file.getName(),
						file.getAbsolutePath(), recursive, classes);
			} else {
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0,
						file.getName().length() - 6);
				try {
					// 添加到集合中去
					classes.add(Class.forName(packageName + '.' + className));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 判断对象中是否存在某个类型的属性
	 * @return
	 */
	public static boolean fieldExist(Object obj,Class<?> field){
		if (null==obj)throw new NullPointerException("被检查的对象不能为空，请检查参数");
		boolean is=false;
		Field[] fields = obj.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			String typeString = fields[i].getType().toString();
			typeString=typeString.substring(typeString.lastIndexOf(".")+1);
			if (field.getSimpleName().equals(typeString)) {
				is=true;
				break;
			}
		}
		return is;
	}
	
	/**
	 * 检测参数是否合法
	 */
	public static void checkFieldName(Object obj,String fieldName){
		if(null==obj)throw new NullPointerException("obj参数不能为空");
		checkFieldName(obj.getClass().getDeclaredFields(), fieldName, true);
	}
	
	public static boolean checkFieldName(Field[] fields,String fieldName,boolean throwException){
		if(null==fields||fields.length==0)throw new NullPointerException("对象中没有任何属性");
		boolean isHave=false;
		for (Field field : fields) {
			if (field.getName().equals(fieldName)) {
				isHave=true;
				break;
			}
		}
		if (!isHave) {
			if(throwException)throw new NullPointerException("注解中声明的属性名称在实体类中不存在，请检查是否输入正确(建议直接在实体类中复制然后粘贴)");
		}
		return isHave;
	}
	
	/**
	 * 克隆一个对象(深拷贝)
	 * @param obj
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
    public static Object deepClone(Object obj) throws IOException, ClassNotFoundException{
        //将对象写到流里
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        //从流里读回来
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        return ois.readObject();
    }
	
    /**
	 * 获取某个类中的某个属性的泛型类型.
	 * @param clz
	 * @param fieldName
	 * @return
	 */
	public static Class<?> getFieldGenericType(Class<?> clz,String fieldName){
//		AnhryException.isNull(clz, "获取属性的反省类型失败！参数clz(属性所属的实体)不能为空");
//		AnhryException.isNull(fieldName, "获取属性的反省类型失败！参数fieldName(需要获取泛型的属性)不能为空");
		Class<?> genericType=null;
		try {
			Field declaredField = clz.getDeclaredField(fieldName);
			ParameterizedType pType=(ParameterizedType) declaredField.getGenericType();
			Type type = pType.getActualTypeArguments()[0];
			genericType=(Class<?>) type;
		} catch (SecurityException e) {
//			e.printStackTrace();
		} catch (NoSuchFieldException e) {
//			e.printStackTrace();
		}
		return genericType;
	}
    
	public static Class<?> getClassGenericType(Object obj){
		return getClassGenericType(obj.getClass());
	}
	
	public static Class<?> getClassGenericType(Class<?> obj){
		Type[] t = obj.getGenericInterfaces();
		if(null==t||t.length==0){
			return null;
		}
		ParameterizedType pt=(ParameterizedType) t[0];
		return (Class<?>)pt.getActualTypeArguments()[0];
	}
	
}