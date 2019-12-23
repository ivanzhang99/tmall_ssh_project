package com.how2java.tmall.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.how2java.tmall.service.BaseService;
import com.how2java.tmall.util.Page;

@Service
public class BaseServiceImpl extends ServiceDelegateDAO implements BaseService {
//	@Autowired
//	DAOImpl dao;

	protected Class clazz;

	public BaseServiceImpl() {
//		System.out.println(this.getClass().getName());
		try {
			throw new Exception();

		} catch (Exception e) {
			// TODO: handle exception
			StackTraceElement stes[] = e.getStackTrace();
			String serviceImpleClassName = stes[1].getClassName();
//			System.out.println(serviceImpleClassName);

			try {
				Class serviceImplClazz = Class.forName(serviceImpleClassName);
				String serviceImpleClassSimpleName = serviceImplClazz.getSimpleName();
				String pojoSimpleName = serviceImpleClassSimpleName.replaceAll("ServiceImpl", "");
//				System.out.println("pojoSimpleName:"+pojoSimpleName);
				String pojoPackageName = serviceImplClazz.getPackage().getName().replaceAll(".service.impl", ".pojo");
				String pojoFullName = pojoPackageName + "." + pojoSimpleName;
				clazz = Class.forName(pojoFullName);
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	/*
	 * public static void main(String[] args) { new
	 * CategoryServiceImpl().showClass(); } public void showClass(){
	 * System.out.println(clazz); }
	 */

	@Override
	public Integer save(Object object) {
		// TODO Auto-generated method stub
		return (Integer) super.save(object);
	}

	/*
	 * @Override public void update(Object object) { // TODO Auto-generated method
	 * stub update(object); }
	 * 
	 * @Override public void delete(Object object) { // TODO Auto-generated method
	 * stub delete(object); }
	 */

	@Override
	public Object get(Class clazz, int id) {
		// TODO Auto-generated method stub
		return super.get(clazz, id);
	}

	@Override
	public Object get(int id) {
		// TODO Auto-generated method stub
		return get(clazz, id);
	}

	@Override
	public List list() {
		// TODO Auto-generated method stub
		DetachedCriteria dc = DetachedCriteria.forClass(clazz);
		dc.addOrder(Order.desc("id"));
		return findByCriteria(dc);
	}

	@Override
	public List<Object> listByPage(Page page) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = DetachedCriteria.forClass(clazz);
		dc.addOrder(Order.desc("id"));
		return findByCriteria(dc, page.getStart(), page.getCount());
	}

	@Override
	public int total() {
		// TODO Auto-generated method stub
		String hql = "select count(*)from " + clazz.getName();
		List<Long> l = find(hql);
		if (l.isEmpty())
			return 0;
		Long result = l.get(0);
		return result.intValue();
	}

	@Override
	public List listByParent(Object parent) {
		// TODO Auto-generated method stub
		String parentName = parent.getClass().getSimpleName();
		String parentNameWithFirstLetterLower = StringUtils.uncapitalize(parentName);
		DetachedCriteria dc = DetachedCriteria.forClass(clazz);
		dc.add(Restrictions.eq(parentNameWithFirstLetterLower, parent));
		dc.addOrder(Order.desc("id"));
		return findByCriteria(dc);
	}

	@Override
	public List list(Page page, Object parent) {
		// TODO Auto-generated method stub
		String parentName = parent.getClass().getSimpleName();
		String parentNameWithFirstLetterLower = StringUtils.uncapitalize(parentName);
		DetachedCriteria dc = DetachedCriteria.forClass(clazz);
		dc.add(Restrictions.eq(parentNameWithFirstLetterLower, parent));
		dc.addOrder(Order.desc("id"));
		return findByCriteria(dc, page.getStart(), page.getCount());
	}

	@Override
	public int total(Object parentObject) {
		// TODO Auto-generated method stub
		String parentName = parentObject.getClass().getSimpleName();
		String parentNameWithFirstLetterLower = StringUtils.uncapitalize(parentName);

		String sqlFormat = "select count(*) from %s bean where bean.%s = ?";
		String hql = String.format(sqlFormat, clazz.getName(), parentNameWithFirstLetterLower);
		List<Long> l = this.find(hql, parentObject);
		if (l.isEmpty()) {
			return 0;
		}
		Long result = l.get(0);
		return result.intValue();
	}

	@Override
	public List list(Object... pairParms) {
		// TODO Auto-generated method stub
		HashMap<String, Object> m = new HashMap<String, Object>();
		for (int i = 0; i < pairParms.length; i = i + 2) {
			m.put(pairParms[i].toString(), pairParms[i + 1]);
		}
		DetachedCriteria dc = DetachedCriteria.forClass(clazz);
		Set<String> ks = m.keySet();
		for (String key : ks) {
			if (null == m.get(key)) {
				dc.add(Restrictions.isNull(key));
			} else {
				dc.add(Restrictions.eq(key, m.get(key)));
			}
		}
		dc.addOrder(Order.desc("id"));
		return this.findByCriteria(dc);
	}

}
