package com.mids.service.impl;

import com.mids.mybatis.mapper.OrganizationMapper;
import com.mids.mybatis.model.Organization;
import com.mids.service.OrganizationService;
import com.mids.mybatis.vo.Tree;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrganizationServiceImpl implements OrganizationService {
	private static Logger LOGGER = LoggerFactory.getLogger(OrganizationServiceImpl.class);
	
    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public List<Tree> findTree() {
        List<Tree> trees = new ArrayList<Tree>();

        List<Organization> organizationFather = organizationMapper.findAllByPid0();

        if (organizationFather != null) {
            for (Organization organizationOne : organizationFather) {
                Tree treeOne = new Tree();

                treeOne.setId(new Long(organizationOne.getId()));
                treeOne.setText(organizationOne.getName());
                treeOne.setIconCls(organizationOne.getIcon());

                List<Organization> organizationSon = organizationMapper.findAllByPid(organizationOne.getId());

                if (organizationSon != null) {
                    List<Tree> tree = new ArrayList<Tree>();
                    for (Organization organizationTwo : organizationSon) {
                        Tree treeTwo = new Tree();
                        treeTwo.setId(new Long(organizationTwo.getId()));
                        treeTwo.setText(organizationTwo.getName());
                        treeTwo.setIconCls(organizationTwo.getIcon());
                        tree.add(treeTwo);
                    }
                    treeOne.setChildren(tree);
                } else {
                    treeOne.setState("closed");
                }
                trees.add(treeOne);
            }
        }
        return trees;
    }

    @Override
    public List<Organization> findTreeGrid() {
        return organizationMapper.findAll();
    }

    @Override
    public void add(Organization organization) {
        organizationMapper.insert(organization);
    }

    @Override
    public Organization findById(Integer id) {
        return organizationMapper.findById(id);
    }

    @Override
    public void update(Organization organization) {
        organizationMapper.update(organization);
    }

    @Override
    public void deleteById(Integer id) {
        organizationMapper.deleteById(id);
    }

}
