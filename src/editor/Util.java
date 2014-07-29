//package editor;
//
//import java.io.File;
//import java.io.FileFilter;
//import java.lang.reflect.Modifier;
//import java.util.Set;
//import java.util.Vector;
//
//public class Util
//{
//    private static class ClassFilter implements FileFilter
//    {// filters only those files with a .class extension
//        public ClassFilter(){}
//        public boolean accept(File pathname)
//        {
//            if(pathname.isDirectory()) return false;
//            else
//            {
//                if((pathname.getName().indexOf(".class") != -1)&&
//                   (pathname.getName().indexOf("$") == -1)) return true;
//                else                                        return false;
//            }
//        }
//    }
//    /**
//     * Returns an array of objects which contains all the extensions of the superClassName
//     * class found in the specified folder.
//     * @param folderName - A folder to search for extensions in
//     * @param superClassName - A full (packagewise) name of the super class
//     */
//    public static Object[] loadPlugins(String folderName, String superClassName, boolean doPrint)
//    {
//        Vector v = new Vector();
//        File file = new File(System.getProperty("user.dir")+"/"+folderName);
//        File[] files = file.listFiles(new ClassFilter());
//        String packagePath = folderName.replaceAll("/", ".");
//        for(int i = 0; files != null && i < files.length; i++)
//        {
//            String className = files[i].getName().split(".class")[0];
//            try
//            {
//                Class curClass = Class.forName(packagePath+"."+className);
//                Class superClass = curClass.getSuperclass();
//                boolean isIt = false; // if curClass is a child of TerrShape
//                while(superClass != null)
//                {
//                    if(superClassName.equals(superClass.getName()))
//                    {
//                        isIt = true;
//                        break;
//                    }
//                    superClass = superClass.getSuperclass();
//                }
//                if(isIt && !Modifier.isAbstract(curClass.getModifiers()))
//                {
//                    //curClass should now be the nonabstract instanciancible class inhereting superClassName
//                    //now just test for image existense
//                    File imageFile = new File(System.getProperty("user.dir")+"/"+folderName+"/img/", className+".gif");
//                    if(!imageFile.exists())
//                    {//all good: add Button and add varMods to Hashtable of tp
//                        if(doPrint)
//                            System.out.println(imageFile+" does not exist");
//                    }
//                    Object o = curClass.newInstance();
//                    v.addElement(o);
//                }
//            }
//            catch(Exception e)
//            {
//                if(doPrint)
//                    System.out.println(className+" cannot be instanciated: "+e);
//            }
//        }
//        return v.toArray();
//    }
//    /**
//     * Returns an array of objects which contains all the extensions of the superClassName
//     * class found in the specified folder.
//     * @param folderName - A folder to search for extensions in
//     * @param superClassName - A full (packagewise) name of the super class
//     */
//    public static String[] loadPluginNames(String folderName, String superClassName, boolean doPrint)
//    {
//        Vector<String> nameList = new Vector<String>();
//        File file = new File(System.getProperty("user.dir")+"/"+folderName);
//        File[] files = file.listFiles(new ClassFilter());
//        String packagePath = folderName.replaceAll("/", ".");
//        for(int i = 0; files != null && i < files.length; i++)
//        {
//            String className = files[i].getName().split(".class")[0];
//            try
//            {
//                Class curClass = Class.forName(packagePath+"."+className);
//                Class superClass = curClass.getSuperclass();
//                boolean isIt = false; // if curClass is a child of TerrShape
//                while(superClass != null)
//                {
//                    if(superClassName.equals(superClass.getName()))
//                    {
//                        isIt = true;
//                        break;
//                    }
//                    superClass = superClass.getSuperclass();
//                }
//                if(isIt && !Modifier.isAbstract(curClass.getModifiers()))
//                {
//                    //curClass should now be the nonabstract instanciancible class inhereting superClassName
//                    nameList.add(className);
//                }
//            }
//            catch(Exception e)
//            {
//                if(doPrint)
//                    System.out.println(className+" cannot be instanciated: "+e);
//            }
//        }
//        return nameList.toArray(new String[nameList.size()]);
//    }
//}
