package com.rthymer.mydiary.service.impl;

import com.rthymer.mydiary.dao.DiaryDao;
import com.rthymer.mydiary.entity.DiaryEntry;
import com.rthymer.mydiary.service.DiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DiaryServiceImpl implements DiaryService {

    @Autowired
    private DiaryDao diaryDao;

    @Override
    public List<DiaryEntry> getDiaryEntryList() {
        return diaryDao.queryDiaryEntry();
    }

    @Override
    public DiaryEntry getDiaryById(int diaryId) {
        return diaryDao.queryDiaryById(diaryId);
    }

    @Transactional
    @Override
    public boolean addDiary(DiaryEntry diary) {
        if (diary.getOwnerName() != null && !"".equals(diary.getOwnerName())) {
            diary.setDateTime(new Date());
            try {
                int returnNum = diaryDao.insertDiary(diary);
                if (returnNum > 0) {
                    return true;
                } else {
                    throw new RuntimeException("新增日记失败了。。。");
                }
            } catch (Exception e) {
                throw new RuntimeException("新增日记失败了:" + e.getMessage());
            }
        } else {
            throw new RuntimeException("创建人都没有？");
        }
    }

    @Transactional
    @Override
    public boolean modifyDiary(DiaryEntry diary) {
        if (diary.getDiaryId() != null && diary.getDiaryId() > 0) {
            diary.setDateTime(new Date());
            try {
                int returnNum = diaryDao.updateDiary(diary);
                if (returnNum > 0) {
                    return true;
                } else {
                    throw new RuntimeException("修改日记失败了。。。");
                }
            } catch (Exception e) {
                throw new RuntimeException("修改日记失败了:" + e.getMessage());
            }
        } else {
            throw new RuntimeException("创建人都没有？");
        }
    }

    @Transactional
    @Override
    public boolean deleteDiary(int diaryId) {
        if (diaryId > 0) {
            try {
                int returnNum = diaryDao.deleteDiary(diaryId);
                if (returnNum > 0) {
                    return true;
                } else {
                    throw new RuntimeException("删除日记失败了。。。");
                }
            } catch (Exception e) {
                throw new RuntimeException("删除日记失败了:" + e.getMessage());
            }
        } else {
            throw new RuntimeException("日记ID居然为空？");
        }
    }

    @Transactional
    @Override
    public boolean deleteDiaries(List<Integer> diaryIdList) {
        for (int diaryId : diaryIdList) {
            if (diaryId <= 0) throw new RuntimeException("日记id不符合要求！");
        }

        try {
            int returnNum = diaryDao.deleteDiaries(diaryIdList);
            if (returnNum > 0) {
                return true;
            } else {
                throw new RuntimeException("删除多项日记失败了。。。");
            }
        } catch (Exception e) {
            throw new RuntimeException("删除多项日记失败了:" + e.getMessage());
        }
    }
}
