package com.merle.dbCompare;//package com.merle.dbCompare;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.OutputStream;
//
///**
// * Created by merle on 2016/8/10.
// */
//public class CompareTable {
//    public static StringBuffer[] sb = {new StringBuffer(),new StringBuffer(),new StringBuffer(),new StringBuffer(),new StringBuffer(),new StringBuffer()};
//
//    public static Transaction getTransaction_product() throws Exception{
//        Class.forName("oracle.jdbc.driver.OracleDriver");
//        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@xxxx:1621:bbbloan", "xxx", "xxx");
//        if(conn !=null) System.out.println("数据库加载成功！");
//        Transaction transaction = new Transaction(conn);
//        return transaction;
//
//    }
//    public static Transaction getTransaction_develop() throws Exception{
//        Class.forName("oracle.jdbc.driver.OracleDriver");
//        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@xxxx:1621:bbbloan", "xxx", "xxx");
//        if(conn !=null) System.out.println("数据库加载成功！");
//        Transaction transaction = new Transaction(conn);
//        return transaction;
//    }
//
//    public static void main(String[] args) throws Exception{
//        compareTables(); //比较数据库
//        writeFile(); //写入文件
//    }
//
//    /**
//     * @author YUJIYU090
//     * 比较生产库和开发库的数据表，包括表名、字段名、字段类型、字段长度
//     * */
//
//    public static void compareTables() throws Exception{
//
//        //1、生产存在，开发不存在的表--跳过
//        //2、生产不存在，开发存在的表--需要人工判断脚本
//        //3、生产存在，开发不存在的字段--需人工判断如何处理
//        //4、生产不存在，开发存在的字段--需要人工判断脚本
//        //5、表和字段都相同，但字段类型不同的内容--需要人工判断脚本
//        //6、表和字段、字段类型都相同，但字段长度不同的内容--需要人工判断脚本
//
//        //生产连接
//        Transaction trans_product = getTransaction_product();
//        Map<String, Table> map_product = getTables(trans_product);
//
//        //开发连接
//        Transaction trans_develop = getTransaction_develop();
//        Map<String, Table> map_develop = getTables(trans_develop);
//
//        //遍历开发库Map
//        for (Iterator<String> iter_table = map_develop.keySet().iterator();  iter_table.hasNext();) {
//            String key_table = (String) iter_table.next();
//            Table table_develop = map_develop.get(key_table);//获得开发库中的表
//            Table table_product = map_product.get(key_table);//尝试从生产库中获得同名表
//            if(table_product == null){ //如果获得表为空，说明开发存在，生产不存在
//                append(table_develop,null,2);
//            }else{ //表相同，判断字段、字段类型、字段长度
//                for(Iterator<String> iter_column = table_develop.columns.keySet().iterator();  iter_column.hasNext();) {
//                    String key_column = (String) iter_column.next();
//                    Column column_develop  = table_develop.columns.get(key_column);//获得开发库中的列
//                    Column column_product  = table_product.columns.get(key_column);//尝试从生产库中获得同名列
//                    if(column_product==null){//如果列名为空，说明开发存在，生产不存在
//                        append(table_develop, column_develop, 4);
//                    }else{//说明两者都存在
//                        if(!column_develop.dataType.equals(column_product.dataType))//字段类型不一致
//                            append(table_develop, column_develop, 5);
//                        if(column_develop.length!=column_product.length)//字段长度不一致
//                            append(table_develop, column_develop, 6);
//                    }
//                }
//            }
//        }
//
//        //遍历生产库Map
//        for (Iterator<String> iter_table = map_product.keySet().iterator();  iter_table.hasNext();) {
//            String key_table = (String) iter_table.next();
//            Table table_product = map_product.get(key_table);//尝试从生产库中获得同名表
//            Table table_develop = map_develop.get(key_table);//获得开发库中的表
//            if(table_develop == null){ //如果获得表为空，说明开发存在，生产不存在
//                append(table_product,null,1);
//            }else{ //表相同，判断字段、字段类型、字段长度
//                for(Iterator<String> iter_column = table_product.columns.keySet().iterator();  iter_column.hasNext();) {
//                    String key_column = (String) iter_column.next();
//                    Column column_product  = table_product.columns.get(key_column);//获得生产库中的列
//                    Column column_develop  = table_develop.columns.get(key_column);//尝试从开发库中获得同名列
//                    if(column_develop==null){//如果列名为空，说明生产存在，开发不存在
//                        append(table_product, column_product, 3);
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * @author YUJIYU090
//     * 传入数据库连接，返回数据库中所有TABLE对象的MAP
//     * */
//    public static Map<String, Table> getTables(Transaction transaction) throws Exception{
//
//        String sSql = " select table_name,Column_Name,Data_Type,"+
//                " DECODE(DATA_TYPE,'NUMBER',DATA_PRECISION,'VARCHAR2',DATA_LENGTH,'VARCHAR',DATA_LENGTH,'CHAR',DATA_LENGTH,0) Length,"+
//                " NVL(DATA_SCALE, 0) SCALE,DECODE(NULLABLE, 'N', '1', '0') NULLABLE "+
//                " from user_tab_columns where 1=1 And table_name Not Like 'BIN$%' Order By table_name,column_name";
//
//        ASResultSet rs = transaction.getASResultSet(sSql);
//
//        Map<String, Table> map = new HashMap<String, Table>();
//
//        String tableName="";
//        Table table = null;
//        while(rs.next()){
//            if(!tableName.equals(rs.getString("table_name"))){//一张新表
//                tableName = rs.getString("table_name");
//                table = new Table(tableName);
//                Column column = new Column(rs.getString("Column_Name"),rs.getString("Data_Type"),rs.getInt("Length"));
//                table.columns.put(column.columnName, column);
//                map.put(rs.getString("table_name"),table);
//            }else{//已存在的表，增加字段
//                Column column = new Column(rs.getString("Column_Name"),rs.getString("Data_Type"),rs.getInt("Length"));
//                table.columns.put(column.columnName, column);
//            }
//        }
//        if(null!=rs) rs.close();
//        transaction.finalize();
//        return map;
//    }
//
//    /**
//     * @author YUJIYU090
//     * 根据标示位，追加到满足条件的StringBuffer
//     * */
//    public static void append(Table table,Column column, int flag) throws Exception{
//        switch (flag) {
//            case 1:
//                System.out.println("1、生产存在，开发不存在的表："+table.getTableName());// 跳过
//                sb[0].append(table.getTableName()+"\n");
//                break;
//            case 2:
//                System.out.println("2、生产不存在，开发存在的表："+table.getTableName());// 需要人工判断脚本
//                sb[1].append(table.getTableName()+"\n");
//                break;
//            case 3:
//                System.out.println("3、生产存在，开发不存在的字段："+table.getTableName()+" | "+column.getColumnName());//需人工判断如何处理
//                sb[2].append(table.getTableName()+" | "+column.getColumnName()+"\n");
//                break;
//            case 4:
//                System.out.println("4、生产不存在，开发存在的字段："+table.getTableName()+" | "+column.getColumnName());//需要人工判断脚本
//                sb[3].append(table.getTableName()+" | "+column.getColumnName()+"\n");
//                break;
//            case 5:
//                System.out.println("5、表和字段都相同，但字段类型不同的内容："+table.getTableName()+" | "+column.getColumnName()+" | "+column.getDataType());//需要人工判断脚本
//                sb[4].append(table.getTableName()+" | "+column.getColumnName()+" | "+column.getDataType()+"\n");
//                break;
//            case 6:
//                System.out.println("6、表和字段、字段类型都相同，但字段长度不同的内容："+table.getTableName()+" | "+column.getColumnName()+" | "+column.getLength());//需要人工判断脚本
//                sb[5].append(table.getTableName()+" | "+column.getColumnName()+" | "+column.getLength()+"\n");
//                break;
//        }
//    }
//
//    /**
//     * @author YUJIYU090
//     * 将StringBuffer中的值写入文件中
//     * */
//    public static void writeFile() throws Exception{
//        String[] fileName = {
//                "D://table//生产存在，开发不存在的表.txt",
//                "D://table//生产不存在，开发存在的表.txt",
//                "D://table//生产存在，开发不存在的字段.txt",
//                "D://table//生产不存在，开发存在的字段.txt",
//                "D://table//表和字段都相同，但字段类型不同的内容.txt",
//                "D://table//表和字段、字段类型都相同，但字段长度不同的内容.txt"};
//
//        for (int i = 0; i < fileName.length; i++) {
//            File file = new File(fileName[i]);
//            OutputStream os = new FileOutputStream(file);
//            os.write(sb[i].toString().getBytes());os.flush();os.close();
//        }
//    }
//}
