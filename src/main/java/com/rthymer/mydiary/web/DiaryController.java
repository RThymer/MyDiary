package com.rthymer.mydiary.web;

import com.rthymer.mydiary.entity.DiaryEntry;
import com.rthymer.mydiary.service.DiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@ResponseBody
@RequestMapping("/admin")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    @RequestMapping(value = "/listdiary", method = RequestMethod.GET)
    private Map<String, Object> listDiary() {
        Map<String, Object> modelMap = new HashMap<>();
        List<DiaryEntry> list = diaryService.getDiaryEntryList();
        modelMap.put("diaryList", list);
        return modelMap;
    }

    @RequestMapping(value = "/getdiarybyid", method = RequestMethod.GET)
    private Map<String, Object> getDiaryById(Integer diaryId) {
        Map<String, Object> modelMap = new HashMap<>();
        DiaryEntry diary = diaryService.getDiaryById(diaryId);
        modelMap.put("diaryList", diary);
        return modelMap;
    }

    @RequestMapping(value = "/adddiary", method = RequestMethod.POST)
    private Map<String, Object> addDiary(@RequestBody DiaryEntry diary) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("success", diaryService.addDiary(diary));
        return modelMap;
    }

    @RequestMapping(value = "/modifydiary", method = RequestMethod.POST)
    private Map<String, Object> modifyDiary(@RequestBody DiaryEntry diary) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("success", diaryService.modifyDiary(diary));
        return modelMap;
    }

    @RequestMapping(value = "/removediary", method = RequestMethod.GET)
    private Map<String, Object> removeDiary(Integer diaryId) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("success", diaryService.deleteDiary(diaryId));
        return modelMap;
    }

}
