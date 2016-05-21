package com.jspxcms.core.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.MapKeyType;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

import com.jspxcms.core.support.Constants;

/**
 * Global
 *
 * @author liufang
 *
 */
@Entity
@Table(name = "cms_global")
public class Global implements java.io.Serializable {
	/**
	 * 模型类型
	 */
	public static final String	MODEL_TYPE			= "global";
	private static final long	serialVersionUID	= 1L;
	/**
	 * 系统预定义字段前缀
	 */
	public static final String	SYS_PREFIX			= "sys_";

	@Transient
	public static void removeAttr(Map<String, String> map, String prefix) {
		Set<String> keysToRemove = new HashSet<String>();
		for (String key : map.keySet()) {
			if (key.startsWith(prefix)) {
				keysToRemove.add(key);
			}
		}
		for (String key : keysToRemove) {
			map.remove(key);
		}
	}

	@Transient
	public static void removeAttrExcludeSys(Map<String, String> map) {
		Set<String> keysToRemove = new HashSet<String>();
		for (String key : map.keySet()) {
			if (!key.startsWith(SYS_PREFIX)) {
				keysToRemove.add(key);
			}
		}
		for (String key : keysToRemove) {
			map.remove(key);
		}
	}

	private Integer				captchaErrors;

	private Map<String, String>	clobs	= new HashMap<String, String>(0);

	private String				contextPath;

	private Map<String, String>	customs	= new HashMap<String, String>(0);

	private String				dataVersion;

	private Integer				id;

	private Integer				port;

	private String				protocol;

	private List<Site>			sites	= new ArrayList<Site>(0);

	private PublishPoint		uploadsPublishPoint;

	private Boolean				withDomain;

	@Column(name = "f_captcha_errors", nullable = false)
	public Integer getCaptchaErrors() {
		return captchaErrors;
	}

	@ElementCollection
	@CollectionTable(name = "cms_global_clob", joinColumns = @JoinColumn(name = "f_global_id"))
	@MapKeyColumn(name = "f_key", length = 50)
	@MapKeyType(value = @Type(type = "string"))
	@Lob
	@Column(name = "f_value", nullable = false)
	public Map<String, String> getClobs() {
		return this.clobs;
	}

	@Transient
	public Object getConf(String className) {
		try {
			return Class.forName(className).getConstructor(Map.class).newInstance(getCustoms());
		} catch (Exception e) {
			throw new IllegalArgumentException("Class '" + className + "' is not Conf Class", e);
		}
	}

	@Length(max = 255)
	@Column(name = "f_context_path", length = 255)
	public String getContextPath() {
		return this.contextPath;
	}

	@ElementCollection
	@CollectionTable(name = "cms_global_custom", joinColumns = @JoinColumn(name = "f_global_id"))
	@MapKeyColumn(name = "f_key", length = 50)
	@Column(name = "f_value", length = 2000)
	public Map<String, String> getCustoms() {
		return this.customs;
	}

	@Length(max = 50)
	@Column(name = "f_version", nullable = false, length = 50)
	public String getDataVersion() {
		return this.dataVersion;
	}

	@Id
	@Column(name = "f_global_id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	@Transient
	public GlobalMail getMail() {
		return new GlobalMail(getCustoms());
	}

	@Transient
	public GlobalOther getOther() {
		return new GlobalOther(getCustoms());
	}

	@Column(name = "f_port")
	public Integer getPort() {
		return this.port;
	}

	@Length(max = 50)
	@Column(name = "f_protocol", length = 50)
	public String getProtocol() {
		return this.protocol;
	}

	@Transient
	public GlobalRegister getRegister() {
		return new GlobalRegister(getCustoms());
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "global")
	@OrderBy("treeNumber asc")
	public List<Site> getSites() {
		return sites;
	}

	/**
	 * 获取模版的显示路径，如为Web上下文发布，则加上上下文路径。如/template, /ctx/template
	 *
	 * @return
	 */
	@Transient
	public String getTemplateDisplayPathByCtx() {
		String ctx = getContextPath();
		StringBuilder sb = new StringBuilder();
		// 存储路径是file:开头代表模版在独立应用里部署，不需加上下文路径。
		if (StringUtils.isNotBlank(ctx) && !StringUtils.startsWith(Constants.TEMPLATE_STORE_PATH, "file:")) {
			sb.append(ctx);
		}
		if (StringUtils.isNotBlank(Constants.TEMPLATE_DISPLAY_PATH)) {
			sb.append(Constants.TEMPLATE_DISPLAY_PATH);
		}
		return sb.toString();
	}

	@Transient
	public GlobalUpload getUpload() {
		return new GlobalUpload(getCustoms());
	}

	@Transient
	public String getUploadsDomain() {
		return getUploadsPublishPoint().getDisplayDomain();
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_uploads_publishpoint_id", nullable = false)
	public PublishPoint getUploadsPublishPoint() {
		return uploadsPublishPoint;
	}

	@Transient
	public Set<String> getValidDomains() {
		Set<String> domains = new HashSet<String>();
		List<Site> sites = getSites();
		for (Site site : sites) {
			domains.add(site.getDomain());
		}
		return domains;
	}

	@Transient
	public String getVersion() {
		return Constants.VERSION;
	}

	@Column(name = "f_is_with_domain", nullable = false, length = 1)
	public Boolean getWithDomain() {
		return this.withDomain;
	}

	@Transient
	public boolean isDocEnabled() {

		if (StringUtils.isBlank(Constants.OPENOFFICE_HOST) || Constants.OPENOFFICE_PORT <= 0
				|| StringUtils.isBlank(Constants.SWFTOOLS_PDF2SWF)) {
			return false;
		} else {
			return true;
		}
	}

	public void setCaptchaErrors(Integer captchaErrors) {
		this.captchaErrors = captchaErrors;
	}

	public void setClobs(Map<String, String> clobs) {
		this.clobs = clobs;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public void setCustoms(Map<String, String> customs) {
		this.customs = customs;
	}

	public void setDataVersion(String dataVersion) {
		this.dataVersion = dataVersion;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public void setSites(List<Site> sites) {
		this.sites = sites;
	}

	public void setUploadsPublishPoint(PublishPoint uploadsPublishPoint) {
		this.uploadsPublishPoint = uploadsPublishPoint;
	}

	public void setWithDomain(Boolean withDomain) {
		this.withDomain = withDomain;
	}
}
