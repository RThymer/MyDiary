package com.rthymer.mydiary.service;

import com.rthymer.mydiary.entity.DiaryEntry;

import java.util.List;

public interface DiaryService {
    /**
     * List the diary entry list
     *
     * @return diaryEntryList
     */
    List<DiaryEntry> getDiaryEntryList();

    /**
     * Query a diary by id
     *
     * @param diaryId
     * @return diaryEntry
     */
    DiaryEntry getDiaryById(int diaryId);

    /**
     * Insert a new diary
     *
     * @param diary
     * @return
     */
    boolean addDiary(DiaryEntry diary);

    /**
     * Update an exist diary
     *
     * @param diary
     * @return
     */
    boolean modifyDiary(DiaryEntry diary);

    /**
     * Delete an exist diary
     *
     * @param diaryId
     * @return
     */
    boolean deleteDiary(int diaryId);

    /**
     * Delete an exist diary
     *
     * @param diaryId
     * @return
     */
    boolean deleteDiaries(List<Integer> diaryIdList);
}
