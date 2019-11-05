package patientsupport.patientsupport.helpers;

import static java.util.stream.Collectors.toMap;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadService {

    private final UploadUtil uploadUtil;
	
	public UploadService(UploadUtil uploadUtil) {
		this.uploadUtil = uploadUtil;
	}

    public List<Map<String, String>> uploadExcelToJson(MultipartFile file) throws Exception {

		Path tempDir = Files.createTempDirectory("");

		File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();
		
		file.transferTo(tempFile);

        // Load excel file
        Workbook workbook = WorkbookFactory.create(tempFile);
        // Load first sheet
		Sheet sheet = workbook.getSheetAt(0);
        // get all rows
		Supplier<Stream<Row>> rowStreamSupplier = uploadUtil.getRowStreamSupplier(sheet);
		// get header file
		Row headerRow = rowStreamSupplier.get().findFirst().get();
		
		List<String> headerCells = uploadUtil.getStream(headerRow)
				.map(String::valueOf)
				.collect(Collectors.toList());
		
        int colCount = headerCells.size();
        
		List<Map<String, String>> result = rowStreamSupplier.get()
				.skip(1)
				.map(row -> {
                    List<String> cellList = uploadUtil.getStream(row)
                            // .map(Cell::getStringCellValue)
                            .map(cell -> {
                                String val = ""; 
                                if(cell.getCellType() == CellType.STRING) {
                                    val = cell.getStringCellValue();
                                }
                                if(cell.getCellType() == CellType.NUMERIC) {
                                    val = String.valueOf(cell.getNumericCellValue());
                                }
                                if(cell.getCellType() == CellType.BLANK) {
                                    
                                }

                                return val;
                            })
							.collect(Collectors.toList());	
					
					return uploadUtil.cellIteratorSupplier(colCount)
							 .get()
							 .collect(toMap(headerCells::get, cellList::get));
                })
                .collect(Collectors.toList());
        
        return result;
	}
    
}