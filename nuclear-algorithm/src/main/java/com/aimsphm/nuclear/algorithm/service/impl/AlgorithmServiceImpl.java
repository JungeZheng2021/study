package com.aimsphm.nuclear.algorithm.service.impl;

import com.aimsphm.nuclear.algorithm.entity.bo.*;
import com.aimsphm.nuclear.algorithm.entity.dto.AlarmEventDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.StateMonitorParamDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.StateMonitorResponseDTO;
import com.aimsphm.nuclear.algorithm.enums.AlgorithmTypeEnum;
import com.aimsphm.nuclear.algorithm.service.AlgorithmAsyncService;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import com.aimsphm.nuclear.algorithm.service.AlgorithmService;
import com.aimsphm.nuclear.common.entity.*;
import com.aimsphm.nuclear.common.service.*;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.*;
import static com.aimsphm.nuclear.common.constant.SymbolConstant.DASH;

/**
 * @Package: com.aimsphm.nuclear.algorithm.service.impl
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/12/23 16:16
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/23 16:16
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Service
public class AlgorithmServiceImpl implements AlgorithmService {
    @Resource
    private CommonDeviceService deviceService;
    @Resource
    private CommonMeasurePointService pointsService;
    @Resource
    private AlgorithmConfigService configService;
    @Resource
    private AlgorithmDeviceModelService deviceModelService;
    @Resource
    private AlgorithmModelPointService pointService;
    @Resource
    private AlgorithmModelService modelService;
    @Resource
    private JobDeviceStatusService statusService;
    @Resource
    private JobAlarmEventService eventService;
    @Resource
    private JobAlarmRealtimeService realtimeService;
    @Resource
    private AlgorithmAsyncService asyncService;
    @Resource(name = "HCM-PAF")
    private AlgorithmHandlerService handlerService;

    @Resource
    private HBaseUtil hBase;

    @Override
    public void getDeviceStateMonitorInfo() {
//        String s = "{\"invokingTime\":1600336200000,\"algorithmPeriod\":60,\"subSystemId\":null,\"deviceId\":3,\"deviceName\":\"3号主送风机\",\"sensorData\":[{\"pointId\":\"6M2DVC302MV-N-vec-Rms\",\"importance\":5,\"thresholdLow\":[null,null,null],\"thresholdHigh\":[null,null,null],\"cells\":[{\"timestamp\":1600336140000,\"value\":44.0},{\"timestamp\":1600336141000,\"value\":43.5},{\"timestamp\":1600336142000,\"value\":43.0},{\"timestamp\":1600336143000,\"value\":44.0},{\"timestamp\":1600336144000,\"value\":44.0},{\"timestamp\":1600336145000,\"value\":44.0},{\"timestamp\":1600336146000,\"value\":43.4375},{\"timestamp\":1600336147000,\"value\":43.0},{\"timestamp\":1600336148000,\"value\":44.0},{\"timestamp\":1600336149000,\"value\":43.26190476190475},{\"timestamp\":1600336150000,\"value\":44.0},{\"timestamp\":1600336151000,\"value\":43.95454545454545},{\"timestamp\":1600336152000,\"value\":43.86734693877551},{\"timestamp\":1600336153000,\"value\":43.875},{\"timestamp\":1600336154000,\"value\":43.0},{\"timestamp\":1600336155000,\"value\":44.0},{\"timestamp\":1600336156000,\"value\":44.0},{\"timestamp\":1600336157000,\"value\":43.19318181818181},{\"timestamp\":1600336158000,\"value\":44.0},{\"timestamp\":1600336159000,\"value\":44.0},{\"timestamp\":1600336160000,\"value\":44.0},{\"timestamp\":1600336161000,\"value\":43.75},{\"timestamp\":1600336162000,\"value\":44.0},{\"timestamp\":1600336163000,\"value\":43.884615384615394},{\"timestamp\":1600336164000,\"value\":43.83333333333333},{\"timestamp\":1600336165000,\"value\":44.0},{\"timestamp\":1600336166000,\"value\":43.0},{\"timestamp\":1600336167000,\"value\":43.125},{\"timestamp\":1600336168000,\"value\":44.0},{\"timestamp\":1600336169000,\"value\":43.75},{\"timestamp\":1600336170000,\"value\":44.0},{\"timestamp\":1600336171000,\"value\":43.34375},{\"timestamp\":1600336172000,\"value\":43.0},{\"timestamp\":1600336173000,\"value\":43.5},{\"timestamp\":1600336174000,\"value\":44.0},{\"timestamp\":1600336175000,\"value\":44.0},{\"timestamp\":1600336176000,\"value\":43.1},{\"timestamp\":1600336177000,\"value\":43.83333333333333},{\"timestamp\":1600336178000,\"value\":43.5},{\"timestamp\":1600336179000,\"value\":43.0},{\"timestamp\":1600336180000,\"value\":43.0},{\"timestamp\":1600336181000,\"value\":43.08333333333333},{\"timestamp\":1600336182000,\"value\":43.0},{\"timestamp\":1600336183000,\"value\":43.0},{\"timestamp\":1600336184000,\"value\":43.0},{\"timestamp\":1600336185000,\"value\":44.0},{\"timestamp\":1600336186000,\"value\":44.0},{\"timestamp\":1600336187000,\"value\":44.0},{\"timestamp\":1600336188000,\"value\":43.0},{\"timestamp\":1600336189000,\"value\":43.0},{\"timestamp\":1600336190000,\"value\":43.16666666666667},{\"timestamp\":1600336191000,\"value\":44.0},{\"timestamp\":1600336192000,\"value\":43.25},{\"timestamp\":1600336193000,\"value\":43.0},{\"timestamp\":1600336194000,\"value\":43.0},{\"timestamp\":1600336195000,\"value\":43.1},{\"timestamp\":1600336196000,\"value\":43.27777777777778},{\"timestamp\":1600336197000,\"value\":43.0},{\"timestamp\":1600336198000,\"value\":43.452380952380956},{\"timestamp\":1600336199000,\"value\":43.78571428571429}]},{\"pointId\":\"6M2DVC302MV-N-acc-ZeroPeak\",\"importance\":5,\"thresholdLow\":[null,0.17,0.16],\"thresholdHigh\":[null,null,null],\"cells\":[{\"timestamp\":1600336140000,\"value\":0.21387426555156708},{\"timestamp\":1600336141000,\"value\":0.2132297605276108},{\"timestamp\":1600336142000,\"value\":0.2139488011598587},{\"timestamp\":1600336143000,\"value\":0.21419869363307956},{\"timestamp\":1600336144000,\"value\":0.2148958295583725},{\"timestamp\":1600336145000,\"value\":0.2120635211467743},{\"timestamp\":1600336146000,\"value\":0.21417678892612454},{\"timestamp\":1600336147000,\"value\":0.21436751261353493},{\"timestamp\":1600336148000,\"value\":0.2159217745065689},{\"timestamp\":1600336149000,\"value\":0.21392357721924785},{\"timestamp\":1600336150000,\"value\":0.2139531672000885},{\"timestamp\":1600336151000,\"value\":0.214055135846138},{\"timestamp\":1600336152000,\"value\":0.21461084485054016},{\"timestamp\":1600336153000,\"value\":0.21421624223391214},{\"timestamp\":1600336154000,\"value\":0.21376028656959534},{\"timestamp\":1600336155000,\"value\":0.2151369601488113},{\"timestamp\":1600336156000,\"value\":0.2151544988155365},{\"timestamp\":1600336157000,\"value\":0.2145713716745377},{\"timestamp\":1600336158000,\"value\":0.21404525637626648},{\"timestamp\":1600336159000,\"value\":0.21337007482846576},{\"timestamp\":1600336160000,\"value\":0.21500980357329053},{\"timestamp\":1600336161000,\"value\":0.2140759478012721},{\"timestamp\":1600336162000,\"value\":0.2151062786579132},{\"timestamp\":1600336163000,\"value\":0.2133086770772934},{\"timestamp\":1600336164000,\"value\":0.2132166177034378},{\"timestamp\":1600336165000,\"value\":0.2135235220193863},{\"timestamp\":1600336166000,\"value\":0.21446174879868826},{\"timestamp\":1600336167000,\"value\":0.21585599581400555},{\"timestamp\":1600336168000,\"value\":0.21427324414253235},{\"timestamp\":1600336169000,\"value\":0.21564993262290955},{\"timestamp\":1600336170000,\"value\":0.2159097082912922},{\"timestamp\":1600336171000,\"value\":0.21464592218399048},{\"timestamp\":1600336172000,\"value\":0.21471606194972992},{\"timestamp\":1600336173000,\"value\":0.21441791951656344},{\"timestamp\":1600336174000,\"value\":0.21415377408266068},{\"timestamp\":1600336175000,\"value\":0.21453192830085754},{\"timestamp\":1600336176000,\"value\":0.2157770891984304},{\"timestamp\":1600336177000,\"value\":0.21476866801579791},{\"timestamp\":1600336178000,\"value\":0.21505803614854813},{\"timestamp\":1600336179000,\"value\":0.21525973081588745},{\"timestamp\":1600336180000,\"value\":0.21471604704856875},{\"timestamp\":1600336181000,\"value\":0.21509750187397006},{\"timestamp\":1600336182000,\"value\":0.2149089773495992},{\"timestamp\":1600336183000,\"value\":0.21497474610805511},{\"timestamp\":1600336184000,\"value\":0.2134796679019928},{\"timestamp\":1600336185000,\"value\":0.21599629024664566},{\"timestamp\":1600336186000,\"value\":0.2142030894756317},{\"timestamp\":1600336187000,\"value\":0.21508434414863584},{\"timestamp\":1600336188000,\"value\":0.21463715036710104},{\"timestamp\":1600336189000,\"value\":0.2146327594916026},{\"timestamp\":1600336190000,\"value\":0.2120591501394908},{\"timestamp\":1600336191000,\"value\":0.21478182574113208},{\"timestamp\":1600336192000,\"value\":0.2142030894756317},{\"timestamp\":1600336193000,\"value\":0.21440038084983826},{\"timestamp\":1600336194000,\"value\":0.2159623131155968},{\"timestamp\":1600336195000,\"value\":0.21419870853424072},{\"timestamp\":1600336196000,\"value\":0.21525095899899804},{\"timestamp\":1600336197000,\"value\":0.21409348150094348},{\"timestamp\":1600336198000,\"value\":0.21587352454662326},{\"timestamp\":1600336199000,\"value\":0.21445628628134727}]},{\"pointId\":\"6M2DVC003MI\",\"importance\":5,\"thresholdLow\":[null,0.8718,0.8618],\"thresholdHigh\":[null,0.9977,0.9987],\"cells\":[{\"timestamp\":1600336140000,\"value\":0.95469069480896},{\"timestamp\":1600336141000,\"value\":0.9547958970069884},{\"timestamp\":1600336142000,\"value\":0.951955238978068},{\"timestamp\":1600336143000,\"value\":0.955321967601776},{\"timestamp\":1600336144000,\"value\":0.95493616660436},{\"timestamp\":1600336145000,\"value\":0.9518851041793824},{\"timestamp\":1600336146000,\"value\":0.9525865316390992},{\"timestamp\":1600336147000,\"value\":0.9530336558818816},{\"timestamp\":1600336148000,\"value\":0.9557603200276692},{\"timestamp\":1600336149000,\"value\":0.9543750882148744},{\"timestamp\":1600336150000,\"value\":0.9538489778836567},{\"timestamp\":1600336151000,\"value\":0.9536123275756836},{\"timestamp\":1600336152000,\"value\":0.9544275999069214},{\"timestamp\":1600336153000,\"value\":0.950850546360016},{\"timestamp\":1600336154000,\"value\":0.9530248045921326},{\"timestamp\":1600336155000,\"value\":0.9552692621946336},{\"timestamp\":1600336156000,\"value\":0.9548484881718952},{\"timestamp\":1600336157000,\"value\":0.9546906352043152},{\"timestamp\":1600336158000,\"value\":0.952704817056656},{\"timestamp\":1600336159000,\"value\":0.9536210894584656},{\"timestamp\":1600336160000,\"value\":0.9527443448702496},{\"timestamp\":1600336161000,\"value\":0.9558830459912616},{\"timestamp\":1600336162000,\"value\":0.9533361196517944},{\"timestamp\":1600336163000,\"value\":0.9538490176200868},{\"timestamp\":1600336164000,\"value\":0.955795407295227},{\"timestamp\":1600336165000,\"value\":0.952709178129832},{\"timestamp\":1600336166000,\"value\":0.9537262121836344},{\"timestamp\":1600336167000,\"value\":0.9539366960525512},{\"timestamp\":1600336168000,\"value\":0.954217274983724},{\"timestamp\":1600336169000,\"value\":0.9533755779266356},{\"timestamp\":1600336170000,\"value\":0.95375694334507},{\"timestamp\":1600336171000,\"value\":0.9531301061312358},{\"timestamp\":1600336172000,\"value\":0.949837863445282},{\"timestamp\":1600336173000,\"value\":0.9536561171213788},{\"timestamp\":1600336174000,\"value\":0.9536517411470412},{\"timestamp\":1600336175000,\"value\":0.9542698264122008},{\"timestamp\":1600336176000,\"value\":0.9568650126457214},{\"timestamp\":1600336177000,\"value\":0.953375518321991},{\"timestamp\":1600336178000,\"value\":0.9531520158052444},{\"timestamp\":1600336179000,\"value\":0.9530073205629984},{\"timestamp\":1600336180000,\"value\":0.9532703161239624},{\"timestamp\":1600336181000,\"value\":0.9553482234477996},{\"timestamp\":1600336182000,\"value\":0.9536912242571514},{\"timestamp\":1600336183000,\"value\":0.9547257224718728},{\"timestamp\":1600336184000,\"value\":0.9537613193194072},{\"timestamp\":1600336185000,\"value\":0.9533755580584208},{\"timestamp\":1600336186000,\"value\":0.952849507331848},{\"timestamp\":1600336187000,\"value\":0.9545328617095948},{\"timestamp\":1600336188000,\"value\":0.9539541999499004},{\"timestamp\":1600336189000,\"value\":0.9553219477335612},{\"timestamp\":1600336190000,\"value\":0.9519376556078594},{\"timestamp\":1600336191000,\"value\":0.9552342295646667},{\"timestamp\":1600336192000,\"value\":0.9549010396003724},{\"timestamp\":1600336193000,\"value\":0.9540594220161438},{\"timestamp\":1600336194000,\"value\":0.9552692969640096},{\"timestamp\":1600336195000,\"value\":0.9543926119804382},{\"timestamp\":1600336196000,\"value\":0.9556901653607686},{\"timestamp\":1600336197000,\"value\":0.9551992217699689},{\"timestamp\":1600336198000,\"value\":0.9539015889167786},{\"timestamp\":1600336199000,\"value\":0.952454999089241}]},{\"pointId\":\"6M2DVC301MV-N-vec-Rms\",\"importance\":5,\"thresholdLow\":[null,null,null],\"thresholdHigh\":[null,52.4,53.4],\"cells\":[{\"timestamp\":1600336140000,\"value\":21.18218421936035},{\"timestamp\":1600336141000,\"value\":21.23010019353918},{\"timestamp\":1600336142000,\"value\":21.199472427368164},{\"timestamp\":1600336143000,\"value\":21.16075255654075},{\"timestamp\":1600336144000,\"value\":21.17118263244629},{\"timestamp\":1600336145000,\"value\":21.20733070373535},{\"timestamp\":1600336146000,\"value\":21.20733070373535},{\"timestamp\":1600336147000,\"value\":21.20733070373535},{\"timestamp\":1600336148000,\"value\":21.18218421936035},{\"timestamp\":1600336149000,\"value\":21.18218421936035},{\"timestamp\":1600336150000,\"value\":24.82046440320137},{\"timestamp\":1600336151000,\"value\":24.83676290512085},{\"timestamp\":1600336152000,\"value\":24.880817413330078},{\"timestamp\":1600336153000,\"value\":21.223048448562626},{\"timestamp\":1600336154000,\"value\":24.85564422607422},{\"timestamp\":1600336155000,\"value\":21.183646224265875},{\"timestamp\":1600336156000,\"value\":24.878569807325093},{\"timestamp\":1600336157000,\"value\":21.21603591625507},{\"timestamp\":1600336158000,\"value\":24.879978307088212},{\"timestamp\":1600336159000,\"value\":21.16242626735142},{\"timestamp\":1600336160000,\"value\":21.18218421936035},{\"timestamp\":1600336161000,\"value\":21.20733070373535},{\"timestamp\":1600336162000,\"value\":21.19475746154785},{\"timestamp\":1600336163000,\"value\":21.205235163370766},{\"timestamp\":1600336164000,\"value\":24.774399670687586},{\"timestamp\":1600336165000,\"value\":21.19475746154785},{\"timestamp\":1600336166000,\"value\":24.746293723583218},{\"timestamp\":1600336167000,\"value\":21.232479095458984},{\"timestamp\":1600336168000,\"value\":21.280678272247318},{\"timestamp\":1600336169000,\"value\":21.238492385200832},{\"timestamp\":1600336170000,\"value\":21.20883960723877},{\"timestamp\":1600336171000,\"value\":24.729772567749023},{\"timestamp\":1600336172000,\"value\":24.71718597412109},{\"timestamp\":1600336173000,\"value\":21.232479095458984},{\"timestamp\":1600336174000,\"value\":24.654253005981445},{\"timestamp\":1600336175000,\"value\":24.607954229627342},{\"timestamp\":1600336176000,\"value\":24.654253005981445},{\"timestamp\":1600336177000,\"value\":24.69269314327755},{\"timestamp\":1600336178000,\"value\":24.62908172607422},{\"timestamp\":1600336179000,\"value\":24.5787353515625},{\"timestamp\":1600336180000,\"value\":24.5787353515625},{\"timestamp\":1600336181000,\"value\":24.5787353515625},{\"timestamp\":1600336182000,\"value\":24.60390853881836},{\"timestamp\":1600336183000,\"value\":21.20733070373535},{\"timestamp\":1600336184000,\"value\":21.20733070373535},{\"timestamp\":1600336185000,\"value\":24.556434480767496},{\"timestamp\":1600336186000,\"value\":24.51517596244812},{\"timestamp\":1600336187000,\"value\":24.47553100585937},{\"timestamp\":1600336188000,\"value\":24.427703857421875},{\"timestamp\":1600336189000,\"value\":24.37736320495605},{\"timestamp\":1600336190000,\"value\":24.352191925048828},{\"timestamp\":1600336191000,\"value\":24.330674540612005},{\"timestamp\":1600336192000,\"value\":24.346914076036025},{\"timestamp\":1600336193000,\"value\":24.28507010142008},{\"timestamp\":1600336194000,\"value\":24.27038764953613},{\"timestamp\":1600336195000,\"value\":24.30185127258301},{\"timestamp\":1600336196000,\"value\":24.3270206451416},{\"timestamp\":1600336197000,\"value\":24.3270206451416},{\"timestamp\":1600336198000,\"value\":24.3270206451416},{\"timestamp\":1600336199000,\"value\":24.30185127258301}]},{\"pointId\":\"6M2DVC301MV-N-acc-ZeroPeak\",\"importance\":5,\"thresholdLow\":[null,2000.0,1136.0],\"thresholdHigh\":[null,null,null],\"cells\":[{\"timestamp\":1600336140000,\"value\":2547.1365966796875},{\"timestamp\":1600336141000,\"value\":2520.2342529296875},{\"timestamp\":1600336142000,\"value\":2505.576009114583},{\"timestamp\":1600336143000,\"value\":2537.724039713542},{\"timestamp\":1600336144000,\"value\":2543.9985758463545},{\"timestamp\":1600336145000,\"value\":2558.5839436848955},{\"timestamp\":1600336146000,\"value\":2486.9140625},{\"timestamp\":1600336147000,\"value\":2528.3776448567705},{\"timestamp\":1600336148000,\"value\":2543.625447591145},{\"timestamp\":1600336149000,\"value\":2505.047882080078},{\"timestamp\":1600336150000,\"value\":2522.5054931640625},{\"timestamp\":1600336151000,\"value\":2588.268961588542},{\"timestamp\":1600336152000,\"value\":2561.887166341145},{\"timestamp\":1600336153000,\"value\":2511.6201171875},{\"timestamp\":1600336154000,\"value\":2564.4599202473955},{\"timestamp\":1600336155000,\"value\":2493.815185546875},{\"timestamp\":1600336156000,\"value\":2557.2642211914067},{\"timestamp\":1600336157000,\"value\":2523.338623046875},{\"timestamp\":1600336158000,\"value\":2518.8123779296875},{\"timestamp\":1600336159000,\"value\":2496.873942057292},{\"timestamp\":1600336160000,\"value\":2508.331970214844},{\"timestamp\":1600336161000,\"value\":2580.100056966145},{\"timestamp\":1600336162000,\"value\":2482.446014404297},{\"timestamp\":1600336163000,\"value\":2515.45361328125},{\"timestamp\":1600336164000,\"value\":2564.5262451171875},{\"timestamp\":1600336165000,\"value\":2503.864501953125},{\"timestamp\":1600336166000,\"value\":2553.41552734375},{\"timestamp\":1600336167000,\"value\":2461.251627604167},{\"timestamp\":1600336168000,\"value\":2524.1694742838545},{\"timestamp\":1600336169000,\"value\":2509.396484375},{\"timestamp\":1600336170000,\"value\":2540.067199707031},{\"timestamp\":1600336171000,\"value\":2580.143310546875},{\"timestamp\":1600336172000,\"value\":2574.27978515625},{\"timestamp\":1600336173000,\"value\":2533.319620768229},{\"timestamp\":1600336174000,\"value\":2573.7626953125},{\"timestamp\":1600336175000,\"value\":2586.63330078125},{\"timestamp\":1600336176000,\"value\":2596.079427083333},{\"timestamp\":1600336177000,\"value\":2517.5607503255205},{\"timestamp\":1600336178000,\"value\":2554.7917887369795},{\"timestamp\":1600336179000,\"value\":2589.783996582031},{\"timestamp\":1600336180000,\"value\":2619.354309082031},{\"timestamp\":1600336181000,\"value\":2547.7634684244795},{\"timestamp\":1600336182000,\"value\":2541.2990315755205},{\"timestamp\":1600336183000,\"value\":2526.622884114583},{\"timestamp\":1600336184000,\"value\":2533.042805989583},{\"timestamp\":1600336185000,\"value\":2540.692749023437},{\"timestamp\":1600336186000,\"value\":2564.1707763671875},{\"timestamp\":1600336187000,\"value\":2563.057983398437},{\"timestamp\":1600336188000,\"value\":2589.926025390625},{\"timestamp\":1600336189000,\"value\":2546.1363525390625},{\"timestamp\":1600336190000,\"value\":2572.247639973958},{\"timestamp\":1600336191000,\"value\":2531.834065755208},{\"timestamp\":1600336192000,\"value\":2529.912292480469},{\"timestamp\":1600336193000,\"value\":2593.63525390625},{\"timestamp\":1600336194000,\"value\":2564.198974609375},{\"timestamp\":1600336195000,\"value\":2593.402628580729},{\"timestamp\":1600336196000,\"value\":2531.478922526042},{\"timestamp\":1600336197000,\"value\":2534.9917399088545},{\"timestamp\":1600336198000,\"value\":2579.7323811848955},{\"timestamp\":1600336199000,\"value\":2603.8671468098955}]},{\"pointId\":\"6M2DVC303MV-N-vec-Rms\",\"importance\":5,\"thresholdLow\":[null,null,null],\"thresholdHigh\":[null,130.0,140.0],\"cells\":[{\"timestamp\":1600336140000,\"value\":63.69644748455429},{\"timestamp\":1600336141000,\"value\":63.69379964695497},{\"timestamp\":1600336142000,\"value\":63.69115180935569},{\"timestamp\":1600336143000,\"value\":63.688503971756404},{\"timestamp\":1600336144000,\"value\":63.68585613415711},{\"timestamp\":1600336145000,\"value\":63.68320829655781},{\"timestamp\":1600336146000,\"value\":63.680560458958524},{\"timestamp\":1600336147000,\"value\":63.67791262135921},{\"timestamp\":1600336148000,\"value\":63.67526478375993},{\"timestamp\":1600336149000,\"value\":63.67261694616063},{\"timestamp\":1600336150000,\"value\":63.669969108561354},{\"timestamp\":1600336151000,\"value\":63.66732127096205},{\"timestamp\":1600336152000,\"value\":63.66467343336275},{\"timestamp\":1600336153000,\"value\":63.66202559576347},{\"timestamp\":1600336154000,\"value\":63.65937775816417},{\"timestamp\":1600336155000,\"value\":63.65672992056487},{\"timestamp\":1600336156000,\"value\":63.65408208296557},{\"timestamp\":1600336157000,\"value\":63.65143424536629},{\"timestamp\":1600336158000,\"value\":63.64878640776699},{\"timestamp\":1600336159000,\"value\":63.646138570167686},{\"timestamp\":1600336160000,\"value\":63.64349073256841},{\"timestamp\":1600336161000,\"value\":63.64084289496911},{\"timestamp\":1600336162000,\"value\":63.63819505736981},{\"timestamp\":1600336163000,\"value\":63.63554721977053},{\"timestamp\":1600336164000,\"value\":63.63289938217123},{\"timestamp\":1600336165000,\"value\":63.630251544571934},{\"timestamp\":1600336166000,\"value\":63.627603706972636},{\"timestamp\":1600336167000,\"value\":63.62495586937335},{\"timestamp\":1600336168000,\"value\":63.62230803177405},{\"timestamp\":1600336169000,\"value\":63.619660194174756},{\"timestamp\":1600336170000,\"value\":63.61701235657546},{\"timestamp\":1600336171000,\"value\":63.61436451897618},{\"timestamp\":1600336172000,\"value\":63.61171668137688},{\"timestamp\":1600336173000,\"value\":63.60906884377758},{\"timestamp\":1600336174000,\"value\":63.606421006178294},{\"timestamp\":1600336175000,\"value\":63.603773168578996},{\"timestamp\":1600336176000,\"value\":63.6011253309797},{\"timestamp\":1600336177000,\"value\":63.59847749338039},{\"timestamp\":1600336178000,\"value\":63.59582965578112},{\"timestamp\":1600336179000,\"value\":63.59318181818182},{\"timestamp\":1600336180000,\"value\":63.59053398058252},{\"timestamp\":1600336181000,\"value\":63.587886142983244},{\"timestamp\":1600336182000,\"value\":63.58523830538394},{\"timestamp\":1600336183000,\"value\":63.58259046778464},{\"timestamp\":1600336184000,\"value\":63.57994263018535},{\"timestamp\":1600336185000,\"value\":63.57729479258606},{\"timestamp\":1600336186000,\"value\":63.574646954986754},{\"timestamp\":1600336187000,\"value\":63.571999117387456},{\"timestamp\":1600336188000,\"value\":63.56935127978818},{\"timestamp\":1600336189000,\"value\":63.56670344218888},{\"timestamp\":1600336190000,\"value\":63.564055604589576},{\"timestamp\":1600336191000,\"value\":63.56140776699029},{\"timestamp\":1600336192000,\"value\":63.558759929391},{\"timestamp\":1600336193000,\"value\":63.556112091791704},{\"timestamp\":1600336194000,\"value\":63.55346425419241},{\"timestamp\":1600336195000,\"value\":63.550816416593115},{\"timestamp\":1600336196000,\"value\":63.54816857899382},{\"timestamp\":1600336197000,\"value\":63.545520741394526},{\"timestamp\":1600336198000,\"value\":63.54287290379522},{\"timestamp\":1600336199000,\"value\":63.54022506619594}]},{\"pointId\":\"6M2DVC303MV-N-acc-ZeroPeak\",\"importance\":5,\"thresholdLow\":[null,null,null],\"thresholdHigh\":[null,130.0,140.0],\"cells\":[{\"timestamp\":1600336140000,\"value\":62.37077997671712},{\"timestamp\":1600336141000,\"value\":62.36745384999168},{\"timestamp\":1600336142000,\"value\":62.36412772326625},{\"timestamp\":1600336143000,\"value\":62.360801596540824},{\"timestamp\":1600336144000,\"value\":62.3574754698154},{\"timestamp\":1600336145000,\"value\":62.35414934308997},{\"timestamp\":1600336146000,\"value\":62.350823216364546},{\"timestamp\":1600336147000,\"value\":62.34749708963912},{\"timestamp\":1600336148000,\"value\":62.34417096291369},{\"timestamp\":1600336149000,\"value\":62.340844836188246},{\"timestamp\":1600336150000,\"value\":62.33751870946283},{\"timestamp\":1600336151000,\"value\":62.3341925827374},{\"timestamp\":1600336152000,\"value\":62.330866456011975},{\"timestamp\":1600336153000,\"value\":62.32754032928655},{\"timestamp\":1600336154000,\"value\":62.32421420256112},{\"timestamp\":1600336155000,\"value\":62.32088807583568},{\"timestamp\":1600336156000,\"value\":62.31756194911026},{\"timestamp\":1600336157000,\"value\":62.31423582238482},{\"timestamp\":1600336158000,\"value\":62.31090969565941},{\"timestamp\":1600336159000,\"value\":62.30758356893397},{\"timestamp\":1600336160000,\"value\":62.304257442208545},{\"timestamp\":1600336161000,\"value\":62.30093131548313},{\"timestamp\":1600336162000,\"value\":62.297605188757686},{\"timestamp\":1600336163000,\"value\":62.29427906203226},{\"timestamp\":1600336164000,\"value\":62.29095293530683},{\"timestamp\":1600336165000,\"value\":62.28762680858141},{\"timestamp\":1600336166000,\"value\":62.284300681855974},{\"timestamp\":1600336167000,\"value\":62.280974555130555},{\"timestamp\":1600336168000,\"value\":62.277648428405115},{\"timestamp\":1600336169000,\"value\":62.27432230167969},{\"timestamp\":1600336170000,\"value\":62.27099617495426},{\"timestamp\":1600336171000,\"value\":62.267670048228844},{\"timestamp\":1600336172000,\"value\":62.26434392150341},{\"timestamp\":1600336173000,\"value\":62.26101779477799},{\"timestamp\":1600336174000,\"value\":62.25769166805256},{\"timestamp\":1600336175000,\"value\":62.25436554132713},{\"timestamp\":1600336176000,\"value\":62.251039414601685},{\"timestamp\":1600336177000,\"value\":62.24771328787625},{\"timestamp\":1600336178000,\"value\":62.24438716115085},{\"timestamp\":1600336179000,\"value\":62.2410610344254},{\"timestamp\":1600336180000,\"value\":62.237734907699995},{\"timestamp\":1600336181000,\"value\":62.23440878097456},{\"timestamp\":1600336182000,\"value\":62.231082654249114},{\"timestamp\":1600336183000,\"value\":62.2277565275237},{\"timestamp\":1600336184000,\"value\":62.224430400798276},{\"timestamp\":1600336185000,\"value\":62.22110427407285},{\"timestamp\":1600336186000,\"value\":62.217778147347424},{\"timestamp\":1600336187000,\"value\":62.214452020622},{\"timestamp\":1600336188000,\"value\":62.21112589389656},{\"timestamp\":1600336189000,\"value\":62.20779976717112},{\"timestamp\":1600336190000,\"value\":62.2044736404457},{\"timestamp\":1600336191000,\"value\":62.20114751372027},{\"timestamp\":1600336192000,\"value\":62.197821386994846},{\"timestamp\":1600336193000,\"value\":62.19449526026942},{\"timestamp\":1600336194000,\"value\":62.191169133543994},{\"timestamp\":1600336195000,\"value\":62.18784300681856},{\"timestamp\":1600336196000,\"value\":62.184516880093135},{\"timestamp\":1600336197000,\"value\":62.1811907533677},{\"timestamp\":1600336198000,\"value\":62.17786462664228},{\"timestamp\":1600336199000,\"value\":62.17453849991685}]}],\"modelEstimateResult\":[{\"modelId\":1,\"modelName\":\"3PAF-fan-non-drive-end\",\"estimateTotal\":[{\"estimateResults\":[{\"pointId\":\"6M2DVC302MV-N-vec-Rms\",\"actual\":2587.9523518880205,\"estimate\":2590.5910931732246,\"residual\":-2.6387412852041052,\"alarmCode\":1000000,\"timeStamp\":1600332840000},{\"pointId\":\"6M2DVC302MV-N-acc-ZeroPeak\",\"actual\":43.0,\"estimate\":43.06114041578171,\"residual\":-0.0611404157817077,\"alarmCode\":1000000,\"timeStamp\":1600332840000},{\"pointId\":\"6M2DVC003MI\",\"actual\":62.7091431499579,\"estimate\":62.91078107857609,\"residual\":-0.2016379286181902,\"alarmCode\":1000000,\"timeStamp\":1600332840000}]}]},{\"modelId\":2,\"modelName\":\"3PAF-fan-drive-end\",\"estimateTotal\":[]},{\"modelId\":3,\"modelName\":\"3PAF-motor-drive-end\",\"estimateTotal\":[]}],\"txAlarmEvent\":[{\"alarmCode\":\"1101104\",\"modelId\":1,\"deviceId\":3,\"deviceName\":\"2号主送风机\",\"subSystemId\":2,\"deviceCode\":\"6M2DVC003ZV\",\"alarmLevel\":4,\"lastAlarm\":1599717600000,\"firstAlarm\":1599717600000,\"alarmContent\":\"风机非驱动端测点报警\",\"alarmStatus\":0,\"stopFlag\":false,\"alarmCount\":1,\"sensorPointIds\":\"6M2DVC302MV-N-vec-Rms\",\"isAlgorithmAlarm\":true,\"alarmFrequency\":0.0,\"alarmType\":5,\"alarmReason\":\"-1\",\"remark\":null,\"realTimeAlarms\":[],\"displaysensorPointIds\":null,\"createBy\":\"period-algorithm\",\"createOn\":1599717656000,\"lastUpdateBy\":\"period-algorithm\",\"lastUpdateOn\":1599717656000,\"delimit\":0,\"id\":100210},{\"alarmCode\":\"1101201\",\"modelId\":-1,\"deviceId\":26,\"deviceName\":\"设备冷却水泵A\",\"subSystemId\":9,\"deviceCode\":\"20-CCS-MP-01A\",\"alarmLevel\":5,\"lastAlarm\":1599717600000,\"firstAlarm\":1598503140000,\"alarmContent\":\"其他相关测点报警\",\"alarmStatus\":0,\"stopFlag\":false,\"alarmCount\":79,\"sensorPointIds\":\"20CCS-PT112\",\"isAlgorithmAlarm\":false,\"alarmFrequency\":0.0039027764054935282,\"alarmType\":1,\"alarmReason\":\"-1\",\"remark\":null,\"realTimeAlarms\":[],\"displaysensorPointIds\":null,\"createBy\":\"period-algorithm\",\"createOn\":1599717656000,\"lastUpdateBy\":\"period-algorithm\",\"lastUpdateOn\":1599717656000,\"delimit\":0,\"id\":99994},{\"alarmCode\":\"1401301\",\"modelId\":-1,\"deviceId\":26,\"deviceName\":\"设备冷却水泵A\",\"subSystemId\":9,\"deviceCode\":\"20-CCS-MP-01A\",\"alarmLevel\":2,\"lastAlarm\":1598588280000,\"firstAlarm\":1598588100000,\"alarmContent\":\"转速与电信号测点报警\",\"alarmStatus\":0,\"stopFlag\":false,\"alarmCount\":4,\"sensorPointIds\":\"20CCS-MP-01A-AMPB\",\"isAlgorithmAlarm\":false,\"alarmFrequency\":1.0,\"alarmType\":7,\"alarmReason\":\"-1\",\"remark\":null,\"realTimeAlarms\":[],\"displaysensorPointIds\":null,\"createBy\":\"period-algorithm\",\"createOn\":1598588301000,\"lastUpdateBy\":\"period-algorithm\",\"lastUpdateOn\":1598588301000,\"delimit\":0,\"id\":100033},{\"alarmCode\":\"1401301\",\"modelId\":-1,\"deviceId\":26,\"deviceName\":\"设备冷却水泵A\",\"subSystemId\":9,\"deviceCode\":\"20-CCS-MP-01A\",\"alarmLevel\":2,\"lastAlarm\":1598588280000,\"firstAlarm\":1598588100000,\"alarmContent\":\"温度相关测点报警\",\"alarmStatus\":0,\"stopFlag\":false,\"alarmCount\":4,\"sensorPointIds\":\"20CCS-MP-01A-TMP1\",\"isAlgorithmAlarm\":false,\"alarmFrequency\":1.0,\"alarmType\":7,\"alarmReason\":\"-1\",\"remark\":null,\"realTimeAlarms\":[],\"displaysensorPointIds\":null,\"createBy\":\"period-algorithm\",\"createOn\":1598588301000,\"lastUpdateBy\":\"period-algorithm\",\"lastUpdateOn\":1598588301000,\"delimit\":0,\"id\":100038},{\"alarmCode\":\"1401301\",\"modelId\":-1,\"deviceId\":26,\"deviceName\":\"设备冷却水泵A\",\"subSystemId\":9,\"deviceCode\":\"20-CCS-MP-01A\",\"alarmLevel\":2,\"lastAlarm\":1598588280000,\"firstAlarm\":1598588100000,\"alarmContent\":\"温度相关测点报警\",\"alarmStatus\":0,\"stopFlag\":false,\"alarmCount\":4,\"sensorPointIds\":\"20CCS-MP-01A-TMP2\",\"isAlgorithmAlarm\":false,\"alarmFrequency\":1.0,\"alarmType\":7,\"alarmReason\":\"-1\",\"remark\":null,\"realTimeAlarms\":[],\"displaysensorPointIds\":null,\"createBy\":\"period-algorithm\",\"createOn\":1598588301000,\"lastUpdateBy\":\"period-algorithm\",\"lastUpdateOn\":1598588301000,\"delimit\":0,\"id\":100039},{\"alarmCode\":\"1401301\",\"modelId\":-1,\"deviceId\":26,\"deviceName\":\"设备冷却水泵A\",\"subSystemId\":9,\"deviceCode\":\"20-CCS-MP-01A\",\"alarmLevel\":2,\"lastAlarm\":1598588280000,\"firstAlarm\":1598588100000,\"alarmContent\":\"温度相关测点报警\",\"alarmStatus\":0,\"stopFlag\":false,\"alarmCount\":4,\"sensorPointIds\":\"20CCS-MP-01A-TMP3\",\"isAlgorithmAlarm\":false,\"alarmFrequency\":1.0,\"alarmType\":7,\"alarmReason\":\"-1\",\"remark\":null,\"realTimeAlarms\":[],\"displaysensorPointIds\":null,\"createBy\":\"period-algorithm\",\"createOn\":1598588301000,\"lastUpdateBy\":\"period-algorithm\",\"lastUpdateOn\":1598588301000,\"delimit\":0,\"id\":100040},{\"alarmCode\":\"1401301\",\"modelId\":-1,\"deviceId\":26,\"deviceName\":\"设备冷却水泵A\",\"subSystemId\":9,\"deviceCode\":\"20-CCS-MP-01A\",\"alarmLevel\":2,\"lastAlarm\":1598588280000,\"firstAlarm\":1598588100000,\"alarmContent\":\"温度相关测点报警\",\"alarmStatus\":0,\"stopFlag\":false,\"alarmCount\":4,\"sensorPointIds\":\"20CCS-MP-01A-TMP4\",\"isAlgorithmAlarm\":false,\"alarmFrequency\":1.0,\"alarmType\":7,\"alarmReason\":\"-1\",\"remark\":null,\"realTimeAlarms\":[],\"displaysensorPointIds\":null,\"createBy\":\"period-algorithm\",\"createOn\":1598588301000,\"lastUpdateBy\":\"period-algorithm\",\"lastUpdateOn\":1598588301000,\"delimit\":0,\"id\":100041},{\"alarmCode\":\"1401301\",\"modelId\":-1,\"deviceId\":26,\"deviceName\":\"设备冷却水泵A\",\"subSystemId\":9,\"deviceCode\":\"20-CCS-MP-01A\",\"alarmLevel\":2,\"lastAlarm\":1598588280000,\"firstAlarm\":1598588100000,\"alarmContent\":\"温度相关测点报警\",\"alarmStatus\":0,\"stopFlag\":false,\"alarmCount\":4,\"sensorPointIds\":\"20CCS-MP-01A-TMP5\",\"isAlgorithmAlarm\":false,\"alarmFrequency\":1.0,\"alarmType\":7,\"alarmReason\":\"-1\",\"remark\":null,\"realTimeAlarms\":[],\"displaysensorPointIds\":null,\"createBy\":\"period-algorithm\",\"createOn\":1598588301000,\"lastUpdateBy\":\"period-algorithm\",\"lastUpdateOn\":1598588301000,\"delimit\":0,\"id\":100042},{\"alarmCode\":\"1401301\",\"modelId\":-1,\"deviceId\":26,\"deviceName\":\"设备冷却水泵A\",\"subSystemId\":9,\"deviceCode\":\"20-CCS-MP-01A\",\"alarmLevel\":2,\"lastAlarm\":1598588280000,\"firstAlarm\":1598588100000,\"alarmContent\":\"其他相关测点报警\",\"alarmStatus\":0,\"stopFlag\":false,\"alarmCount\":4,\"sensorPointIds\":\"20CCS-PT111\",\"isAlgorithmAlarm\":false,\"alarmFrequency\":1.0,\"alarmType\":7,\"alarmReason\":\"-1\",\"remark\":null,\"realTimeAlarms\":[],\"displaysensorPointIds\":null,\"createBy\":\"period-algorithm\",\"createOn\":1598588301000,\"lastUpdateBy\":\"period-algorithm\",\"lastUpdateOn\":1598588301000,\"delimit\":0,\"id\":100043},{\"alarmCode\":\"1401301\",\"modelId\":-1,\"deviceId\":26,\"deviceName\":\"设备冷却水泵A\",\"subSystemId\":9,\"deviceCode\":\"20-CCS-MP-01A\",\"alarmLevel\":2,\"lastAlarm\":1598588280000,\"firstAlarm\":1598588100000,\"alarmContent\":\"其他相关测点报警\",\"alarmStatus\":0,\"stopFlag\":false,\"alarmCount\":4,\"sensorPointIds\":\"20CCS-PT112\",\"isAlgorithmAlarm\":false,\"alarmFrequency\":1.0,\"alarmType\":7,\"alarmReason\":\"-1\",\"remark\":null,\"realTimeAlarms\":[],\"displaysensorPointIds\":null,\"createBy\":\"period-algorithm\",\"createOn\":1598588301000,\"lastUpdateBy\":\"period-algorithm\",\"lastUpdateOn\":1598588301000,\"delimit\":0,\"id\":100044},{\"alarmCode\":\"1159101,1159201,1159201,1159201,1159203,1159101,1159101,1159203,1159103,1159103\",\"modelId\":59,\"deviceId\":26,\"deviceName\":\"设备冷却水泵A\",\"subSystemId\":9,\"deviceCode\":\"20-CCS-MP-01A\",\"alarmLevel\":3,\"lastAlarm\":1598508120000,\"firstAlarm\":1598504160000,\"alarmContent\":\"温度相关测点报警,其他相关测点报警,转速与电信号测点报警\",\"alarmStatus\":0,\"stopFlag\":false,\"alarmCount\":59,\"sensorPointIds\":\"20CCS-MP-01A-AMPB,20CCS-MP-01A-TMP1,20CCS-MP-01A-TMP2,20CCS-MP-01A-TMP3,20CCS-MP-01A-TMP4,20CCS-MP-01A-TMP5,20CCS-PT111,20CCS-PT112,20CCS-TE121,20CCS-TE122\",\"isAlgorithmAlarm\":true,\"alarmFrequency\":0.8805970149253731,\"alarmType\":5,\"alarmReason\":\"-1\",\"remark\":null,\"realTimeAlarms\":[],\"displaysensorPointIds\":null,\"createBy\":\"period-algorithm\",\"createOn\":1598508171000,\"lastUpdateBy\":\"period-algorithm\",\"lastUpdateOn\":1598508171000,\"delimit\":0,\"id\":99999},{\"alarmCode\":\"1159101,1159201,1159201,1159201,1159202,1159101,1159101,1159202,1159301,1159102\",\"modelId\":59,\"deviceId\":26,\"deviceName\":\"设备冷却水泵A\",\"subSystemId\":9,\"deviceCode\":\"20-CCS-MP-01A\",\"alarmLevel\":3,\"lastAlarm\":1598505360000,\"firstAlarm\":1598505300000,\"alarmContent\":\"温度相关测点报警,其他相关测点报警,转速与电信号测点报警\",\"alarmStatus\":0,\"stopFlag\":false,\"alarmCount\":2,\"sensorPointIds\":\"20CCS-MP-01A-AMPB,20CCS-MP-01A-TMP1,20CCS-MP-01A-TMP2,20CCS-MP-01A-TMP3,20CCS-MP-01A-TMP4,20CCS-MP-01A-TMP5,20CCS-PT111,20CCS-PT112,20CCS-TE121,20CCS-TE122\",\"isAlgorithmAlarm\":true,\"alarmFrequency\":1.0,\"alarmType\":5,\"alarmReason\":\"-1\",\"remark\":null,\"realTimeAlarms\":[],\"displaysensorPointIds\":null,\"createBy\":\"period-algorithm\",\"createOn\":1598505410000,\"lastUpdateBy\":\"period-algorithm\",\"lastUpdateOn\":1598505410000,\"delimit\":0,\"id\":100000},{\"alarmCode\":\"1159101,1159201,1159201,1159201,1159202,1159101,1159101,1159202,1159102\",\"modelId\":59,\"deviceId\":26,\"deviceName\":\"设备冷却水泵A\",\"subSystemId\":9,\"deviceCode\":\"20-CCS-MP-01A\",\"alarmLevel\":3,\"lastAlarm\":1598505240000,\"firstAlarm\":1598504100000,\"alarmContent\":\"温度相关测点报警,其他相关测点报警,转速与电信号测点报警\",\"alarmStatus\":0,\"stopFlag\":false,\"alarmCount\":7,\"sensorPointIds\":\"20CCS-MP-01A-AMPB,20CCS-MP-01A-TMP1,20CCS-MP-01A-TMP2,20CCS-MP-01A-TMP3,20CCS-MP-01A-TMP4,20CCS-MP-01A-TMP5,20CCS-PT111,20CCS-PT112,20CCS-TE122\",\"isAlgorithmAlarm\":true,\"alarmFrequency\":0.35,\"alarmType\":5,\"alarmReason\":\"-1\",\"remark\":null,\"realTimeAlarms\":[],\"displaysensorPointIds\":null,\"createBy\":\"period-algorithm\",\"createOn\":1598505290000,\"lastUpdateBy\":\"period-algorithm\",\"lastUpdateOn\":1598505290000,\"delimit\":0,\"id\":99998},{\"alarmCode\":\"1101201,1101102\",\"modelId\":-1,\"deviceId\":26,\"deviceName\":\"设备冷却水泵A\",\"subSystemId\":9,\"deviceCode\":\"20-CCS-MP-01A\",\"alarmLevel\":5,\"lastAlarm\":1598504820000,\"firstAlarm\":1598504040000,\"alarmContent\":\"温度相关测点报警,其他相关测点报警\",\"alarmStatus\":0,\"stopFlag\":false,\"alarmCount\":7,\"sensorPointIds\":\"20CCS-PT112,20CCS-TE122\",\"isAlgorithmAlarm\":false,\"alarmFrequency\":0.5,\"alarmType\":1,\"alarmReason\":\"-1\",\"remark\":null,\"realTimeAlarms\":[],\"displaysensorPointIds\":null,\"createBy\":\"period-algorithm\",\"createOn\":1598504870000,\"lastUpdateBy\":\"period-algorithm\",\"lastUpdateOn\":1598504870000,\"delimit\":0,\"id\":99997},{\"alarmCode\":\"1159101,1159201,1159201,1159201,1159101,1159101\",\"modelId\":59,\"deviceId\":26,\"deviceName\":\"设备冷却水泵A\",\"subSystemId\":9,\"deviceCode\":\"20-CCS-MP-01A\",\"alarmLevel\":3,\"lastAlarm\":1598504040000,\"firstAlarm\":1598503500000,\"alarmContent\":\"温度相关测点报警,其他相关测点报警,转速与电信号测点报警\",\"alarmStatus\":0,\"stopFlag\":false,\"alarmCount\":10,\"sensorPointIds\":\"20CCS-MP-01A-AMPB,20CCS-MP-01A-TMP1,20CCS-MP-01A-TMP2,20CCS-MP-01A-TMP3,20CCS-MP-01A-TMP5,20CCS-PT111\",\"isAlgorithmAlarm\":true,\"alarmFrequency\":1.0,\"alarmType\":5,\"alarmReason\":\"-1\",\"remark\":null,\"realTimeAlarms\":[],\"displaysensorPointIds\":null,\"createBy\":\"period-algorithm\",\"createOn\":1598504090000,\"lastUpdateBy\":\"period-algorithm\",\"lastUpdateOn\":1598504090000,\"delimit\":0,\"id\":99996},{\"alarmCode\":\"1101201\",\"modelId\":-1,\"deviceId\":26,\"deviceName\":\"设备冷却水泵A\",\"subSystemId\":9,\"deviceCode\":\"20-CCS-MP-01A\",\"alarmLevel\":5,\"lastAlarm\":1598503260000,\"firstAlarm\":1598503200000,\"alarmContent\":\"其他相关测点报警\",\"alarmStatus\":0,\"stopFlag\":false,\"alarmCount\":2,\"sensorPointIds\":\"20CCS-PT112\",\"isAlgorithmAlarm\":false,\"alarmFrequency\":1.0,\"alarmType\":1,\"alarmReason\":\"-1\",\"remark\":null,\"realTimeAlarms\":[],\"displaysensorPointIds\":null,\"createBy\":\"period-algorithm\",\"createOn\":1598503310000,\"lastUpdateBy\":\"period-algorithm\",\"lastUpdateOn\":1598503310000,\"delimit\":0,\"id\":99995},{\"alarmCode\":\"1101103,1101103\",\"modelId\":-1,\"deviceId\":26,\"deviceName\":\"主泵1B\",\"subSystemId\":1,\"deviceCode\":\"10-RCS-MP-01B\",\"alarmLevel\":1,\"alarmContent\":\"振动相关测点报警\",\"alarmStatus\":0,\"stopFlag\":false,\"alarmCount\":4,\"sensorPointIds\":\"10RCS-YIA232C,10RCS-YIA232D\",\"isAlgorithmAlarm\":false,\"alarmFrequency\":1.1852554225435582E-4,\"alarmType\":1,\"alarmReason\":\"-1\",\"realTimeAlarms\":[]}]}";

//        StateMonitorParamDTO paramDTO = JSON.parseObject(s, StateMonitorParamDTO.class);
//        List<StateMonitorParamDTO> data = Lists.newArrayList(paramDTO);//operateParams(AlgorithmTypeEnum.STATE_MONITOR.getType());
        List<StateMonitorParamDTO> data = operateParams(AlgorithmTypeEnum.STATE_MONITOR.getType());
        if (CollectionUtils.isEmpty(data)) {
            return;
        }
        long invokingTime = System.currentTimeMillis();
        int period = 3600;
        data.stream().filter(Objects::nonNull).forEach(item -> {
            if (item.getDeviceId() == 3) {
                item.setAlgorithmPeriod(period);
                item.setInvokingTime(invokingTime);
                StateMonitorResponseDTO response = (StateMonitorResponseDTO) handlerService.getInvokeCustomerData(item);
                saveResult(response);
            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    void saveResult(StateMonitorResponseDTO response) {
        if (Objects.isNull(response)) {
            return;
        }
        Long deviceId = response.getDeviceId();
        Integer healthStatus = response.getHealthStatus();
        updateDeviceStatus(deviceId, healthStatus);
        saveAlarmEvent(response.getTxAlarmEvent());
        saveEstimateResult(response.getModelEstimateResult());
    }

    /**
     * 存储算法调用结果
     *
     * @param result
     */
    private void saveEstimateResult(List<EstimateResponseDataBO> result) {
        if (CollectionUtils.isEmpty(result)) {
            return;
        }
        result.stream().filter(Objects::nonNull).forEach(item -> {
            List<PointEstimateResultsDataBO> bos = item.getModelEstimateResult();
            if (CollectionUtils.isEmpty(bos)) {
                return;
            }
            PointEstimateResultsDataBO dataBO = bos.get(0);
            if (Objects.isNull(dataBO)) {
                return;
            }
            List<PointEstimateDataBO> dataList = dataBO.getEstimateResults();
            if (CollectionUtils.isEmpty(dataList)) {
                return;
            }
            Long modelId = item.getModelId();
            dataList.stream().filter(Objects::nonNull).forEach(x -> {
                Long timestamp = x.getTimestamp();
                try {
                    hBase.insertObject(H_BASE_TABLE_NPC_PHM_DATA, modelId + ROW_KEY_SEPARATOR + timestamp, H_BASE_FAMILY_NPC_ESTIMATE, x.getPointId(), x, timestamp);
                } catch (IOException e) {
                    log.error("HBase insert failed...{}", e);
                }
            });
        });
    }

    /**
     * 保存报警事件
     *
     * @param eventList
     */
    private void saveAlarmEvent(List<AlarmEventDTO> eventList) {
        if (CollectionUtils.isEmpty(eventList)) {
            return;
        }
        List<JobAlarmRealtimeDO> realTimeAlarms = Lists.newArrayList();
        List<JobAlarmEventDO> alarmList = Lists.newArrayList();
        eventList.stream().filter(item -> CollectionUtils.isNotEmpty(item.getRealTimeAlarms())).forEach(item -> {
            realTimeAlarms.addAll(item.getRealTimeAlarms());
            JobAlarmEventDO eventDO = new JobAlarmEventDO();
            BeanUtils.copyProperties(item, eventDO);
            alarmList.add(eventDO);
        });
        eventService.saveBatch(alarmList);
        realtimeService.saveBatch(realTimeAlarms);
    }

    /**
     * 更新设备状态
     *
     * @param deviceId     设备id
     * @param healthStatus 健康状态
     */
    private void updateDeviceStatus(Long deviceId, Integer healthStatus) {
        CommonDeviceDO device = deviceService.getById(deviceId);
        if (Objects.isNull(device)) {
            return;
        }
        JobDeviceStatusDO status = statusService.getDeviceRunningStatus(deviceId);
        //保存一个新的状态
        if (Objects.isNull(status)) {
            JobDeviceStatusDO newOne = new JobDeviceStatusDO();
            newOne.setStatus(healthStatus);
            newOne.setDeviceId(deviceId);
            newOne.setGmtStart(new Date());
            statusService.save(newOne);
            return;

        }
        if (status.equals(healthStatus)) {
            return;
        }
        Date currentDate = new Date();
        status.setGmtEnd(currentDate);
        status.setStatusDuration(currentDate.getTime() - status.getGmtStart().getTime());
        //更新结束时间
        statusService.updateById(status);
        status.setGmtEnd(null);
        status.setGmtStart(currentDate);
        status.setStatus(healthStatus);
        statusService.save(status);
    }

    /**
     * 获取状态监测算法、设备、模型、测点之间的数据
     *
     * @return
     */
    private List<StateMonitorParamDTO> operateParams(String type) {
        LambdaQueryWrapper<AlgorithmConfigDO> wrapper = Wrappers.lambdaQuery(AlgorithmConfigDO.class);
        wrapper.eq(AlgorithmConfigDO::getAlgorithmType, type);
        AlgorithmConfigDO algorithmConfig = configService.getOne(wrapper);
        if (Objects.isNull(algorithmConfig)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<AlgorithmDeviceModelDO> deviceModelWrapper = Wrappers.lambdaQuery(AlgorithmDeviceModelDO.class);
        deviceModelWrapper.eq(AlgorithmDeviceModelDO::getAlgorithmId, algorithmConfig.getId());
        List<AlgorithmDeviceModelDO> deviceModelList = deviceModelService.list(deviceModelWrapper);
        if (CollectionUtils.isEmpty(deviceModelList)) {
            return Lists.newArrayList();
        }
        //设备层级
        List<StateMonitorParamDTO> collect = deviceModelList.stream().map(x -> {
            CommonDeviceDO device = deviceService.getById(x.getDeviceId());
            if (Objects.isNull(device)) {
                return null;
            }
            StateMonitorParamDTO param = new StateMonitorParamDTO();
            param.setDeviceName(device.getDeviceName());
            param.setDeviceId(x.getDeviceId());
            param.setTxAlarmEvent(listAlarmEvent(x.getDeviceId()));
            Long id = x.getId();
            LambdaQueryWrapper<AlgorithmModelDO> modelWrapper = Wrappers.lambdaQuery(AlgorithmModelDO.class);
            modelWrapper.eq(AlgorithmModelDO::getDeviceModelId, id);
            List<AlgorithmModelDO> modelList = modelService.list(modelWrapper);
            if (CollectionUtils.isEmpty(modelList)) {
                return param;
            }
            List<AlgorithmModelPointDO> pointList = Lists.newArrayList();
            param.setModelEstimateResult(listEstimateData(modelList, pointList));
            param.setSensorData(listPointData(pointList));
            return param;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return collect;
    }

    private List<JobAlarmEventDO> listAlarmEvent(Long deviceId) {
        LambdaQueryWrapper<JobAlarmEventDO> wrapper = Wrappers.lambdaQuery(JobAlarmEventDO.class);
        wrapper.eq(JobAlarmEventDO::getDeviceId, deviceId);
        wrapper.ge(JobAlarmEventDO::getGmtLastAlarm, System.currentTimeMillis() - 30 * 86400000).le(JobAlarmEventDO::getGmtLastAlarm, System.currentTimeMillis());
        return eventService.list(wrapper);
    }

    private List<EstimateParamDataBO> listEstimateData(List<AlgorithmModelDO> modelList, List<AlgorithmModelPointDO> pointList) {
        return modelList.stream().map(m -> {
            EstimateParamDataBO bo = new EstimateParamDataBO();
            bo.setModelId(m.getId());
            bo.setModelName(m.getModelName());
            LambdaQueryWrapper<AlgorithmModelPointDO> modePointWrapper = Wrappers.lambdaQuery(AlgorithmModelPointDO.class);
            modePointWrapper.eq(AlgorithmModelPointDO::getModelId, m.getId());
            List<AlgorithmModelPointDO> pointDOS = pointService.list(modePointWrapper);
            if (CollectionUtils.isEmpty(pointDOS)) {
                return bo;
            }
            pointList.addAll(pointDOS);
            LambdaQueryWrapper<CommonMeasurePointDO> pointsWrapper = Wrappers.lambdaQuery(CommonMeasurePointDO.class);
            pointsWrapper.select(CommonMeasurePointDO::getPointId);
            pointsWrapper.in(CommonMeasurePointDO::getId, pointList.stream().map(item -> item.getPointId()).collect(Collectors.toList()));
            List<CommonMeasurePointDO> list = pointsService.list(pointsWrapper);
            bo.setEstimateTotal(listPointEstimateResultsData(m.getId(), list));
            return bo;
        }).collect(Collectors.toList());
    }

    private List<PointEstimateResultsDataBO> listPointEstimateResultsData(Long modelId, List<CommonMeasurePointDO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
        PointEstimateResultsDataBO bo = new PointEstimateResultsDataBO();
        List<PointEstimateResultsDataBO> data = Lists.newArrayList(bo);
        List<String> ids = Lists.newArrayList(list.stream().filter(Objects::nonNull).map(x -> x.getPointId()).collect(Collectors.toSet()));
        try {
            List<PointEstimateDataBO> collect = hBase.selectModelDataList(H_BASE_TABLE_NPC_PHM_DATA, System.currentTimeMillis() - 15 * 86400000
                    , System.currentTimeMillis(), H_BASE_FAMILY_NPC_ESTIMATE, ids, modelId);
            bo.setEstimateResults(collect);
        } catch (IOException e) {
            log.error("select Estimate data failed..{}", e);
        }
        return data;
    }

    private List<PointDataBO> listPointData(List<AlgorithmModelPointDO> pointList) {
        if (CollectionUtils.isEmpty(pointList)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<CommonMeasurePointDO> pointsWrapper = Wrappers.lambdaQuery(CommonMeasurePointDO.class);
        pointsWrapper.select(CommonMeasurePointDO::getImportance, CommonMeasurePointDO::getPointId, CommonMeasurePointDO::getPointType,
                CommonMeasurePointDO::getSensorCode, CommonMeasurePointDO::getFeatureType, CommonMeasurePointDO::getFeature,
                CommonMeasurePointDO::getEarlyWarningLow, CommonMeasurePointDO::getThresholdLow, CommonMeasurePointDO::getThresholdLower,
                CommonMeasurePointDO::getEarlyWarningHigh, CommonMeasurePointDO::getThresholdHigh, CommonMeasurePointDO::getThresholdHigher);
        pointsWrapper.in(CommonMeasurePointDO::getId, pointList.stream().map(item -> item.getPointId()).collect(Collectors.toSet()));
        List<CommonMeasurePointDO> list = pointsService.list(pointsWrapper);
        CountDownLatch countDownLatch = new CountDownLatch(list.size());
        List<PointDataBO> collect = Lists.newArrayList();
        for (CommonMeasurePointDO item : list) {
            PointDataBO data = new PointDataBO();
            data.setImportance(item.getImportance());
            data.setPointId(item.getPointId());
            //低低报值，低报值，低预警值
            data.setThresholdLow(Lists.newArrayList(item.getThresholdLower(), item.getThresholdLow(), item.getEarlyWarningLow()));
            //高高报值，高报值，高预警值
            data.setThresholdHigh(Lists.newArrayList(item.getThresholdHigher(), item.getThresholdHigh(), item.getEarlyWarningHigh()));
            String family = item.getPointType() == 1 ? H_BASE_FAMILY_NPC_PI_REAL_TIME : item.getFeatureType() + DASH + item.getFeature();
            asyncService.listPointDataFromHBase(family, item.getSensorCode(), data, countDownLatch);
            collect.add(data);
            log.info("测试异步是否生成： " + System.currentTimeMillis());
        }
        try {
            countDownLatch.await();
            log.info("最后完成： " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return collect;
    }
}
