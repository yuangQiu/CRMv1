package com.cdsxt.dao.impl;

import com.cdsxt.dao.ResourceDao;
import com.cdsxt.po.Resource;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public class ResourceDaoImpl implements ResourceDao {

    @Autowired
    private SessionFactory sessionFactory;

    public Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Resource> queryAllResource() {
        return this.getSession().createQuery("from Resource cr").list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Resource> queryResourceOnePage(int startRow, int pageRow) {
        Query query = this.getSession().createQuery("select cr from Resource cr");
        query.setFirstResult(startRow);
        query.setMaxResults(pageRow);
        return query.list();
    }

    @Override
    public Resource queryResourceById(int id) {
        Query query = this.getSession().createQuery("from Resource cr where cr.id = :id").setInteger("id", id);
        return (Resource) query.uniqueResult();
    }

    @Override
    public void deleteResource(Resource resource) {
        this.getSession().delete(resource);
    }

    @Override
    public void modifyResource(Resource resource) {
        this.getSession().update(resource);
    }

    @Override
    public void addResource(Resource resource) {
        this.getSession().save(resource);
    }

}