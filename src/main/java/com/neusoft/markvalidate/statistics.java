package com.neusoft.markvalidate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import com.neusipo.controller.ResultController;
import com.neusipo.domain.Result;
import com.neusipo.domain.SortData;
import com.neusoft.markvalidate.utils.DBUtil;
import com.neusoft.markvalidate.utils.IDGenerator;

/**
 * 
 * <p>[描述信息：对标引过得标题（拆的词）组成的检索式调用统计接口返回排序情况]</p>
 *
 * @author 范丹平
 * @mail fandp@neusoft.com
 * @version 1.0 Created on 2018-10-12 上午08:31:56
 */
public class statistics {
	/**
	 * 统计方法
	 */
	public static void validate() {
		//获取数据库连接
		Connection conn=DBUtil.getConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		ResultController resultController = new ResultController();
		//查询100条检索式记录,调用接口,将返回排序文献集插入表 ，通过Hitnum判断结果集是否为空或大于1000或正常
		try {
			String sql="select * from sipo_mp_ti_mark";
			ps= conn.prepareStatement(sql);
			rs=ps.executeQuery();
			while(rs.next()){
				//String id=rs.getString(1);
				//String type=rs.getString(2);
				//String userId=rs.getString(3);
				String an=rs.getString(4);
				String word=rs.getString(5);
				//String createTime=rs.getString(6);
				String citedAn=rs.getString(7);
				//System.out.println(id +","+type+","+userId+","+an+","+word+","+createTime+","+citedAn);
				Result result=resultController.getResult(word,an,citedAn);
				System.out.println("result：haha"+result);
				String  id= IDGenerator.generate();
				String insertsql="";
                if(result.getHitNum()==0){
                 	int hitcount= 0;
                 	int location_cited=-1;
                 	insertsql="insert into sipo_markvalidatestatistics(id,an,cited_an,word,hitcount,location_cited) values ('"+id+"','"+an+"','"+citedAn+"','"+word+"','"+hitcount+"','"+location_cited+"')";
                	 ps=conn.prepareStatement(insertsql);
					int a=ps.executeUpdate();
                }else if(result.getHitNum() >1000){
					int hitcount= 1001;
					int location_cited=-1;
					insertsql="insert into sipo_markvalidatestatistics(id,an,cited_an,word,hitcount,location_cited) values ('"+id+"','"+an+"','"+citedAn+"','"+word+"','"+hitcount+"','"+location_cited+"')";
					ps=conn.prepareStatement(insertsql);
					int a=ps.executeUpdate();
                }else{
					List<SortData> sortListData=result.getSortDataList();
					int hitcount= result.getHitNum();
					int location_cited=result.getLocation_com();
					for(int i=0;i<sortListData.size();i++){
						 SortData doc=sortListData.get(i);
                         String sortDocAn=doc.getAn();
                         String sortDocTi=doc.getTi();
                         String sortPd=doc.getPd();
                         int sortScore=doc.getScore();
						 insertsql="insert into sipo_markvalidatestatistics(id,an,cited_an,word,hitcount,location_cited,sort_an,sort_ti,sort_pd,sort_score) values ('"+id+"','"+an+"','"+citedAn+"','"+word+"','"+hitcount+"','"+location_cited+"','"+sortDocAn+"','"+sortDocTi+"','"+sortPd+"','"+sortScore+"')";
						 ps=conn.prepareStatement(insertsql);
						 int a=ps.executeUpdate();
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			resultController.close();
			//关闭连接
			DBUtil.release(conn, ps, rs);
		}
		
	}

	/**
	 * main启动方法
	 * @param args
	 */
	public static void main(String[] args){
		statistics.validate();
	}
  
}
