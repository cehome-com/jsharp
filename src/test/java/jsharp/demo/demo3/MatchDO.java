package jsharp.demo.demo3;

import java.util.Date;
import java.util.Map;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import jsharp.sql.anno.Table;

@Table(name="lottery_match", columnUnderscoreSupport=true)
public class MatchDO extends BaseDO implements Comparable<MatchDO>{

	private static final long serialVersionUID = 5811966033975461958L;

	/**
	 * 主键id
	 */
	@GeneratedValue(generator="class:jsharp.sql.id.TimeGeneration")
	@Id
	private long matchId;

	/**
	 * 彩种id
	 */
	private int lotteryTypeId;

	/**
	 * 彩期id
	 */
	private long issueId;

	/**
	 * 场次序号
	 */
	private long matchOrder;

	/**
	 * 玩法
	 */
	private int playType;

	/**
	 * 比赛时间
	 */
	private Date playTime;

	/**
	 * 截期时间
	 */
	private Date endTime;

	/**
	 * 停售时间
	 */
	private Date lastBuyTime;

	/**
	 * 销售状态
	 */
	private int drawed;

	/**
	 * 中奖名单状态
	 */
	private int luckyOrderFinished;

	/**
	 * 系统算奖状态
	 */
	private int sysCalFinished;

	/**
	 * 系统比对状态
	 */
	private int syncStatus;

	/**
	 * 战绩计算状态
	 */
	private int experienced;

	/**
	 * 赛事名称
	 */
	private String gameName;

	/**
	 * 赛事编号
	 */
	private String gameType;

	/**
	 * 赛事资讯
	 */
	private int playNum;

	/**
	 * 主队名称
	 */
	private String hostTeam;

	/**
	 * 主队全名
	 */
	private String hostTeamFull;

	/**
	 * 客队名称
	 */
	private String visitTeam;

	/**
	 * 客队全名
	 */
	private String visitTeamFull;

	/**
	 * 主队资讯
	 */
	private int hostTeamNum;

	/**
	 * 客队资讯
	 */
	private int visitTeamNum;

	/**
	 * 主队排名
	 */
	private String hostTeamRank;

	/**
	 * 客队排名
	 */
	private String visitTeamRank;

	/**
	 * 主队国家id
	 */
	private int hostCountry;

	/**
	 * 客队国家id
	 */
	private int visitCountry;

	/**
	 * 主队半场比分
	 */
	private Integer hostTeamHscore;

	/**
	 * 主队全场比分
	 */
	private Integer hostTeamFscore;

	/**
	 * 客队半场比分
	 */
	private Integer visitTeamHscore;

	/**
	 * 客队全场比分
	 */
	private Integer visitTeamFscore;

	/**
	 * 文字直播
	 */
	private String wzzb;

	/**
	 * 是否推荐
	 */
	private int recommend;

    /**
     * 对阵的扩展数据
     */
    //private Map<String, MatchExtDO> matchExtMap = new HashMap<String, MatchExtDO>(3);
	
	/**
	 * setter for column 主键id
	 */
	public void setMatchId(long matchId) {
		this.matchId = matchId;
	}

	/**
	 * getter for column 主键id
	 */
	public long getMatchId() {
		return this.matchId;
	}

	/**
	 * setter for column 彩种id
	 */
	public void setLotteryTypeId(int lotteryTypeId) {
		this.lotteryTypeId = lotteryTypeId;
	}

	/**
	 * getter for column 彩种id
	 */
	public int getLotteryTypeId() {
		return this.lotteryTypeId;
	}

	/**
	 * setter for column 彩期id
	 */
	public void setIssueId(long issueId) {
		this.issueId = issueId;
	}

	/**
	 * getter for column 彩期id
	 */
	public long getIssueId() {
		return this.issueId;
	}

	/**
	 * setter for column 场次序号
	 */
	public void setMatchOrder(long matchOrder) {
		this.matchOrder = matchOrder;
	}

	/**
	 * getter for column 场次序号
	 */
	public long getMatchOrder() {
		return this.matchOrder;
	}

	/**
	 * setter for column 玩法
	 */
	public void setPlayType(int playType) {
		this.playType = playType;
	}

	/**
	 * getter for column 玩法
	 */
	public int getPlayType() {
		return this.playType;
	}

	/**
	 * setter for column 比赛时间
	 */
	public void setPlayTime(Date playTime) {
		this.playTime = playTime;
	}

	/**
	 * getter for column 比赛时间
	 */
	public Date getPlayTime() {
		return this.playTime;
	}

	/**
	 * setter for column 截期时间
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * getter for column 截期时间
	 */
	public Date getEndTime() {
		return this.endTime;
	}

	/**
	 * setter for column 停售时间
	 */
	public void setLastBuyTime(Date lastBuyTime) {
		this.lastBuyTime = lastBuyTime;
	}

	/**
	 * getter for column 停售时间
	 */
	public Date getLastBuyTime() {
		return this.lastBuyTime;
	}

	/**
	 * setter for column 销售状态
	 */
	public void setDrawed(int drawed) {
		this.drawed = drawed;
	}

	/**
	 * getter for column 销售状态
	 */
	public int getDrawed() {
		return this.drawed;
	}

	/**
	 * setter for column 中奖名单状态
	 */
	public void setLuckyOrderFinished(int luckyOrderFinished) {
		this.luckyOrderFinished = luckyOrderFinished;
	}

	/**
	 * getter for column 中奖名单状态
	 */
	public int getLuckyOrderFinished() {
		return this.luckyOrderFinished;
	}

	/**
	 * setter for column 系统算奖状态
	 */
	public void setSysCalFinished(int sysCalFinished) {
		this.sysCalFinished = sysCalFinished;
	}

	/**
	 * getter for column 系统算奖状态
	 */
	public int getSysCalFinished() {
		return this.sysCalFinished;
	}

	/**
	 * setter for column 系统比对状态
	 */
	public void setSyncStatus(int syncStatus) {
		this.syncStatus = syncStatus;
	}

	/**
	 * getter for column 系统比对状态
	 */
	public int getSyncStatus() {
		return this.syncStatus;
	}

	/**
	 * setter for column 战绩计算状态
	 */
	public void setExperienced(int experienced) {
		this.experienced = experienced;
	}

	/**
	 * getter for column 战绩计算状态
	 */
	public int getExperienced() {
		return this.experienced;
	}

	/**
	 * setter for column 赛事名称
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	/**
	 * getter for column 赛事名称
	 */
	public String getGameName() {
		return this.gameName;
	}

	/**
	 * setter for column 赛事编号
	 */
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	/**
	 * getter for column 赛事编号
	 */
	public String getGameType() {
		return this.gameType;
	}

	/**
	 * setter for column 赛事资讯
	 */
	public void setPlayNum(int playNum) {
		this.playNum = playNum;
	}

	/**
	 * getter for column 赛事资讯
	 */
	public int getPlayNum() {
		return this.playNum;
	}

	/**
	 * setter for column 主队名称
	 */
	public void setHostTeam(String hostTeam) {
		this.hostTeam = hostTeam;
	}

	/**
	 * getter for column 主队名称
	 */
	public String getHostTeam() {
		return this.hostTeam;
	}

	/**
	 * setter for column 主队全名
	 */
	public void setHostTeamFull(String hostTeamFull) {
		this.hostTeamFull = hostTeamFull;
	}

	/**
	 * getter for column 主队全名
	 */
	public String getHostTeamFull() {
		return this.hostTeamFull;
	}

	/**
	 * setter for column 客队名称
	 */
	public void setVisitTeam(String visitTeam) {
		this.visitTeam = visitTeam;
	}

	/**
	 * getter for column 客队名称
	 */
	public String getVisitTeam() {
		return this.visitTeam;
	}

	/**
	 * setter for column 客队全名
	 */
	public void setVisitTeamFull(String visitTeamFull) {
		this.visitTeamFull = visitTeamFull;
	}

	/**
	 * getter for column 客队全名
	 */
	public String getVisitTeamFull() {
		return this.visitTeamFull;
	}

	/**
	 * setter for column 主队资讯
	 */
	public void setHostTeamNum(int hostTeamNum) {
		this.hostTeamNum = hostTeamNum;
	}

	/**
	 * getter for column 主队资讯
	 */
	public int getHostTeamNum() {
		return this.hostTeamNum;
	}

	/**
	 * setter for column 客队资讯
	 */
	public void setVisitTeamNum(int visitTeamNum) {
		this.visitTeamNum = visitTeamNum;
	}

	/**
	 * getter for column 客队资讯
	 */
	public int getVisitTeamNum() {
		return this.visitTeamNum;
	}

	/**
	 * setter for column 主队排名
	 */
	public void setHostTeamRank(String hostTeamRank) {
		this.hostTeamRank = hostTeamRank;
	}

	/**
	 * getter for column 主队排名
	 */
	public String getHostTeamRank() {
		return this.hostTeamRank;
	}

	/**
	 * setter for column 客队排名
	 */
	public void setVisitTeamRank(String visitTeamRank) {
		this.visitTeamRank = visitTeamRank;
	}

	/**
	 * getter for column 客队排名
	 */
	public String getVisitTeamRank() {
		return this.visitTeamRank;
	}

	/**
	 * setter for column 主队国家id
	 */
	public void setHostCountry(int hostCountry) {
		this.hostCountry = hostCountry;
	}

	/**
	 * getter for column 主队国家id
	 */
	public int getHostCountry() {
		return this.hostCountry;
	}

	/**
	 * setter for column 客队国家id
	 */
	public void setVisitCountry(int visitCountry) {
		this.visitCountry = visitCountry;
	}

	/**
	 * getter for column 客队国家id
	 */
	public int getVisitCountry() {
		return this.visitCountry;
	}

    public Integer getHostTeamHscore() {
        return hostTeamHscore;
    }

    public void setHostTeamHscore(final Integer hostTeamHscore) {
        this.hostTeamHscore = hostTeamHscore;
    }

    public Integer getHostTeamFscore() {
        return hostTeamFscore;
    }

    public void setHostTeamFscore(final Integer hostTeamFscore) {
        this.hostTeamFscore = hostTeamFscore;
    }

    public Integer getVisitTeamHscore() {
        return visitTeamHscore;
    }

    public void setVisitTeamHscore(final Integer visitTeamHscore) {
        this.visitTeamHscore = visitTeamHscore;
    }

    public Integer getVisitTeamFscore() {
        return visitTeamFscore;
    }

    public void setVisitTeamFscore(final Integer visitTeamFscore) {
        this.visitTeamFscore = visitTeamFscore;
    }

    /**
	 * setter for column 文字直播
	 */
	public void setWzzb(String wzzb) {
		this.wzzb = wzzb;
	}

	/**
	 * getter for column 文字直播
	 */
	public String getWzzb() {
		return this.wzzb;
	}

	/**
	 * setter for column 是否推荐
	 */
	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}

	/**
	 * getter for column 是否推荐
	 */
	public int getRecommend() {
		return this.recommend;
	}

	public int compareTo(MatchDO o) {
		// TODO Auto-generated method stub
		return 0;
	}

     
}
