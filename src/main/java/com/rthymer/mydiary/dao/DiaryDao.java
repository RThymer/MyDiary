package com.rthymer.mydiary.dao;

import com.rthymer.mydiary.entity.DiaryEntry;

import java.util.List;

public interface DiaryDao {

    /**
     * List the diary entry list
     *
     * @return diaryEntryList
     */
    List<DiaryEntry> queryDiaryEntry();

    /**
     * Query a diary by id
     *
     * @param diaryId
     * @return diaryEntry
     */
    DiaryEntry queryDiaryById(int diaryId);

    /**
     * Insert a new diary
     *
     * @param diary
     * @return
     */
    int insertDiary(DiaryEntry diary);

    /**
     * Update an exist diary
     *
     * @param diary
     * @return
     */
    int updateDiary(DiaryEntry diary);

    /**
     * Delete an exist diary
     *
     * @param diaryId
     * @return
     */
    int deleteDiary(int diaryId);
}
