package com.rthymer.mydiary.web;

import com.alibaba.fastjson.JSONObject;
import com.rthymer.mydiary.entity.DiaryEntry;
import com.rthymer.mydiary.service.DiaryService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    /* Not Well Coded
    @RequestMapping(value = "/removediaries", method = RequestMethod.POST)
    private Map<String, Object> removeDiaries(@RequestBody Map<String,Object> params) {
        Object values = params.get("diaryIdList");

        @SuppressWarnings("unchecked")
        List<String> list = (ArrayList<String>) values;

        int[] diaryIdList = new int[list.size()];
        for (int index = 0; index < list.size(); index++) {
            diaryIdList[index] = Integer.parseInt(list.get(index));
        }

        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("success", diaryService.deleteDiaries(diaryIdList));
        return modelMap;
    }
    */

    @RequestMapping(value = "/removediaries", method = RequestMethod.POST)
    private Map<String, Object> removeDiaries(@RequestBody Map<String, List<Integer>> params) {
        List<Integer> idList = params.get("diaryIdList");
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("success", diaryService.deleteDiaries(idList));
        return modelMap;
    }
}
