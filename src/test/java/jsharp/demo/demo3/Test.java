package jsharp.demo.demo3;

import javax.sql.DataSource;

import jsharp.demo.DemoUtils;
import jsharp.sql.ObjectSessionFactory;
import jsharp.sql.SessionFactory;


public class Test {

	static DataSource ds=DemoUtils.getDataSource();
	static SessionFactory factory=new ObjectSessionFactory() ; 
	public static void main(String[] args) throws Exception {
		factory.setDataSource(ds);
		MatchDO match2=factory.queryOne("{matchId}=?", MatchDO.class,20111207300421L);
		if(match2!=null){
		System.out.println(match2.getMatchOrder());
		return;}
		
		//DataMap m=factory.queryOne("select * from lottery_match where match_id=?", 644352117921L);
		//System.out.println(m.get("match_order"));
		//SimpleDao<MatchDO> sd=new SimpleDao<MatchDO>(factory,MatchDO.class);
		
		MatchDao dao=new MatchDao();
		dao.setSessionFactory(factory); 
		MatchDO match=MatchDO.createEntity(MatchDO.class);
			//	sd.createBean();
		
		match.setLotteryTypeId(21);
		match.setIssueId(14L);
		match.setMatchOrder(12345678);
		match.setPlayType(1);
		match.setPlayTime(new java.util.Date());
		match.setDrawed(0);
		match.setGameName("game 2");
		match.setGameType("game type");
		match.setHostTeam("host");
		match.setVisitTeam("visit");
		match.setMatchId(12345678921L);
		dao.save(match,true);
		
		
		match=factory.queryOne("match_id=?", MatchDO.class,644352117921L);
		match=	dao.getTableDao().queryOneByProps("lottery_match", null, "matchId",644352117921L);
		System.out.println(match.getDrawed());
		match.setDrawed(3);
		dao.save(match,true);
		match=	dao.getTableDao().queryOneByProps("lottery_match", null, "matchId",644352117921L);
		System.out.println(match.getDrawed());
		
		/*Map map= new DataMap();
		map.put("taskName", "12222");
		map.put("agentId",2);
		map.put("typeId", 1);
		System.out.println("map="+map);
		 
		factory.update(Global.TABLE_TASK, map, "id=?", 10);*/
	}

}
