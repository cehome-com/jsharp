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
	 * ����id
	 */
	@GeneratedValue(generator="class:jsharp.sql.id.TimeGeneration")
	@Id
	private long matchId;

	/**
	 * ����id
	 */
	private int lotteryTypeId;

	/**
	 * ����id
	 */
	private long issueId;

	/**
	 * �������
	 */
	private long matchOrder;

	/**
	 * �淨
	 */
	private int playType;

	/**
	 * ����ʱ��
	 */
	private Date playTime;

	/**
	 * ����ʱ��
	 */
	private Date endTime;

	/**
	 * ͣ��ʱ��
	 */
	private Date lastBuyTime;

	/**
	 * ����״̬
	 */
	private int drawed;

	/**
	 * �н�����״̬
	 */
	private int luckyOrderFinished;

	/**
	 * ϵͳ�㽱״̬
	 */
	private int sysCalFinished;

	/**
	 * ϵͳ�ȶ�״̬
	 */
	private int syncStatus;

	/**
	 * ս������״̬
	 */
	private int experienced;

	/**
	 * ��������
	 */
	private String gameName;

	/**
	 * ���±��
	 */
	private String gameType;

	/**
	 * ������Ѷ
	 */
	private int playNum;

	/**
	 * ��������
	 */
	private String hostTeam;

	/**
	 * ����ȫ��
	 */
	private String hostTeamFull;

	/**
	 * �Ͷ�����
	 */
	private String visitTeam;

	/**
	 * �Ͷ�ȫ��
	 */
	private String visitTeamFull;

	/**
	 * ������Ѷ
	 */
	private int hostTeamNum;

	/**
	 * �Ͷ���Ѷ
	 */
	private int visitTeamNum;

	/**
	 * ��������
	 */
	private String hostTeamRank;

	/**
	 * �Ͷ�����
	 */
	private String visitTeamRank;

	/**
	 * ���ӹ���id
	 */
	private int hostCountry;

	/**
	 * �Ͷӹ���id
	 */
	private int visitCountry;

	/**
	 * ���Ӱ볡�ȷ�
	 */
	private Integer hostTeamHscore;

	/**
	 * ����ȫ���ȷ�
	 */
	private Integer hostTeamFscore;

	/**
	 * �ͶӰ볡�ȷ�
	 */
	private Integer visitTeamHscore;

	/**
	 * �Ͷ�ȫ���ȷ�
	 */
	private Integer visitTeamFscore;

	/**
	 * ����ֱ��
	 */
	private String wzzb;

	/**
	 * �Ƿ��Ƽ�
	 */
	private int recommend;

    /**
     * �������չ����
     */
    //private Map<String, MatchExtDO> matchExtMap = new HashMap<String, MatchExtDO>(3);
	
	/**
	 * setter for column ����id
	 */
	public void setMatchId(long matchId) {
		this.matchId = matchId;
	}

	/**
	 * getter for column ����id
	 */
	public long getMatchId() {
		return this.matchId;
	}

	/**
	 * setter for column ����id
	 */
	public void setLotteryTypeId(int lotteryTypeId) {
		this.lotteryTypeId = lotteryTypeId;
	}

	/**
	 * getter for column ����id
	 */
	public int getLotteryTypeId() {
		return this.lotteryTypeId;
	}

	/**
	 * setter for column ����id
	 */
	public void setIssueId(long issueId) {
		this.issueId = issueId;
	}

	/**
	 * getter for column ����id
	 */
	public long getIssueId() {
		return this.issueId;
	}

	/**
	 * setter for column �������
	 */
	public void setMatchOrder(long matchOrder) {
		this.matchOrder = matchOrder;
	}

	/**
	 * getter for column �������
	 */
	public long getMatchOrder() {
		return this.matchOrder;
	}

	/**
	 * setter for column �淨
	 */
	public void setPlayType(int playType) {
		this.playType = playType;
	}

	/**
	 * getter for column �淨
	 */
	public int getPlayType() {
		return this.playType;
	}

	/**
	 * setter for column ����ʱ��
	 */
	public void setPlayTime(Date playTime) {
		this.playTime = playTime;
	}

	/**
	 * getter for column ����ʱ��
	 */
	public Date getPlayTime() {
		return this.playTime;
	}

	/**
	 * setter for column ����ʱ��
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * getter for column ����ʱ��
	 */
	public Date getEndTime() {
		return this.endTime;
	}

	/**
	 * setter for column ͣ��ʱ��
	 */
	public void setLastBuyTime(Date lastBuyTime) {
		this.lastBuyTime = lastBuyTime;
	}

	/**
	 * getter for column ͣ��ʱ��
	 */
	public Date getLastBuyTime() {
		return this.lastBuyTime;
	}

	/**
	 * setter for column ����״̬
	 */
	public void setDrawed(int drawed) {
		this.drawed = drawed;
	}

	/**
	 * getter for column ����״̬
	 */
	public int getDrawed() {
		return this.drawed;
	}

	/**
	 * setter for column �н�����״̬
	 */
	public void setLuckyOrderFinished(int luckyOrderFinished) {
		this.luckyOrderFinished = luckyOrderFinished;
	}

	/**
	 * getter for column �н�����״̬
	 */
	public int getLuckyOrderFinished() {
		return this.luckyOrderFinished;
	}

	/**
	 * setter for column ϵͳ�㽱״̬
	 */
	public void setSysCalFinished(int sysCalFinished) {
		this.sysCalFinished = sysCalFinished;
	}

	/**
	 * getter for column ϵͳ�㽱״̬
	 */
	public int getSysCalFinished() {
		return this.sysCalFinished;
	}

	/**
	 * setter for column ϵͳ�ȶ�״̬
	 */
	public void setSyncStatus(int syncStatus) {
		this.syncStatus = syncStatus;
	}

	/**
	 * getter for column ϵͳ�ȶ�״̬
	 */
	public int getSyncStatus() {
		return this.syncStatus;
	}

	/**
	 * setter for column ս������״̬
	 */
	public void setExperienced(int experienced) {
		this.experienced = experienced;
	}

	/**
	 * getter for column ս������״̬
	 */
	public int getExperienced() {
		return this.experienced;
	}

	/**
	 * setter for column ��������
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	/**
	 * getter for column ��������
	 */
	public String getGameName() {
		return this.gameName;
	}

	/**
	 * setter for column ���±��
	 */
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	/**
	 * getter for column ���±��
	 */
	public String getGameType() {
		return this.gameType;
	}

	/**
	 * setter for column ������Ѷ
	 */
	public void setPlayNum(int playNum) {
		this.playNum = playNum;
	}

	/**
	 * getter for column ������Ѷ
	 */
	public int getPlayNum() {
		return this.playNum;
	}

	/**
	 * setter for column ��������
	 */
	public void setHostTeam(String hostTeam) {
		this.hostTeam = hostTeam;
	}

	/**
	 * getter for column ��������
	 */
	public String getHostTeam() {
		return this.hostTeam;
	}

	/**
	 * setter for column ����ȫ��
	 */
	public void setHostTeamFull(String hostTeamFull) {
		this.hostTeamFull = hostTeamFull;
	}

	/**
	 * getter for column ����ȫ��
	 */
	public String getHostTeamFull() {
		return this.hostTeamFull;
	}

	/**
	 * setter for column �Ͷ�����
	 */
	public void setVisitTeam(String visitTeam) {
		this.visitTeam = visitTeam;
	}

	/**
	 * getter for column �Ͷ�����
	 */
	public String getVisitTeam() {
		return this.visitTeam;
	}

	/**
	 * setter for column �Ͷ�ȫ��
	 */
	public void setVisitTeamFull(String visitTeamFull) {
		this.visitTeamFull = visitTeamFull;
	}

	/**
	 * getter for column �Ͷ�ȫ��
	 */
	public String getVisitTeamFull() {
		return this.visitTeamFull;
	}

	/**
	 * setter for column ������Ѷ
	 */
	public void setHostTeamNum(int hostTeamNum) {
		this.hostTeamNum = hostTeamNum;
	}

	/**
	 * getter for column ������Ѷ
	 */
	public int getHostTeamNum() {
		return this.hostTeamNum;
	}

	/**
	 * setter for column �Ͷ���Ѷ
	 */
	public void setVisitTeamNum(int visitTeamNum) {
		this.visitTeamNum = visitTeamNum;
	}

	/**
	 * getter for column �Ͷ���Ѷ
	 */
	public int getVisitTeamNum() {
		return this.visitTeamNum;
	}

	/**
	 * setter for column ��������
	 */
	public void setHostTeamRank(String hostTeamRank) {
		this.hostTeamRank = hostTeamRank;
	}

	/**
	 * getter for column ��������
	 */
	public String getHostTeamRank() {
		return this.hostTeamRank;
	}

	/**
	 * setter for column �Ͷ�����
	 */
	public void setVisitTeamRank(String visitTeamRank) {
		this.visitTeamRank = visitTeamRank;
	}

	/**
	 * getter for column �Ͷ�����
	 */
	public String getVisitTeamRank() {
		return this.visitTeamRank;
	}

	/**
	 * setter for column ���ӹ���id
	 */
	public void setHostCountry(int hostCountry) {
		this.hostCountry = hostCountry;
	}

	/**
	 * getter for column ���ӹ���id
	 */
	public int getHostCountry() {
		return this.hostCountry;
	}

	/**
	 * setter for column �Ͷӹ���id
	 */
	public void setVisitCountry(int visitCountry) {
		this.visitCountry = visitCountry;
	}

	/**
	 * getter for column �Ͷӹ���id
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
	 * setter for column ����ֱ��
	 */
	public void setWzzb(String wzzb) {
		this.wzzb = wzzb;
	}

	/**
	 * getter for column ����ֱ��
	 */
	public String getWzzb() {
		return this.wzzb;
	}

	/**
	 * setter for column �Ƿ��Ƽ�
	 */
	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}

	/**
	 * getter for column �Ƿ��Ƽ�
	 */
	public int getRecommend() {
		return this.recommend;
	}

	public int compareTo(MatchDO o) {
		// TODO Auto-generated method stub
		return 0;
	}

     
}
