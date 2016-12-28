package com.oracle.jets.spatial252;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


// TODO: ロギングはJSON形式で標準出力に出す
/**
 * @author hhayakaw
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/directions")
class SpatialController {
 
    @Autowired
    private GeometryService service;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    Direction getDirection(
            @RequestParam double org_lat, @RequestParam double org_lon,
            @RequestParam double dst_lat, @RequestParam double dst_lon) {
        // TODO: RESTのエラー応答等の実装
        return service.getShortestDirection(
                new Point(org_lat, org_lon), new Point(dst_lat, dst_lon));
    }

    @RequestMapping(
            value = "/refuges",
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    List<RefugeWithDirection> getNearestRefuges(
            @RequestParam double org_lat, @RequestParam double org_lon,
            @RequestParam int limit) {
        // TODO: RESTのエラー応答等の実装
        return service.getNearestRefuges(new Point(org_lat, org_lon), limit);
    }

}
