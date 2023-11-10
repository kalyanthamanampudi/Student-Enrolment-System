package studentsinfo.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import studentsinfo.entities.Batch;
import studentsinfo.repositories.BatchRepo;

@RestController
public class BatchController {

	@Autowired
	private BatchRepo batchrepo;

	@GetMapping("/batches")
	@Operation(summary = "Get information about all batches")
	public List<Batch> getBatches() {
		return batchrepo.findAll();
	}

	// 2
	// <---------------add new batch ---------------------->
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/addbatch")
	@Operation(summary = "Add batch", description = "Adding a new batch ")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Batch added successfully"),
			@ApiResponse(responseCode = "400", description = "Bad request, Batch details is incorrect"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public Batch addNewBatch(@Valid @RequestBody Batch newbatch) {
		try {
			List<Batch> existingbatches = batchrepo.findAll();
			boolean exist = existingbatches.contains(newbatch);
			if (exist)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "timimgs in the same day Already Present");
			// newbatch.getStartDate(),newbatch.getDuration()
			batchrepo.save(newbatch);
			return newbatch;
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	// <---------------delete batch---------------------->
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/deletebatch/{id}")
	@Operation(summary = "delete batch", description = "Deleting a batch by using its id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Batch deleted successfully"),
			@ApiResponse(responseCode = "404", description = " Batch id is Not Present"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public void deleteOneBatch(@PathVariable("id") Integer id) {
		try {
			Optional<Batch> batch = batchrepo.findById(id);
			if (batch.isPresent())
				batchrepo.deleteById(id);
			else
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "batch with this id is Not Found!");
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
		} catch (Exception ex) {
			throw ex;
		}
	}

	// <---------------update batch---------------------->
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/updatebatch/{id}")
	@Operation(summary = "Update batch", description = "Updating a batch by using its id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Batch Updated successfully"),
			@ApiResponse(responseCode = "404", description = " Batch id is Not Present"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public void updateOneBatch(@PathVariable("id") Integer id, @RequestBody Batch batch) {
		try {
			Optional<Batch> optbatch = batchrepo.findById(id);
			if (optbatch.isPresent()) {
				Batch b = optbatch.get();
				b.setCode(batch.getCode());
				b.setStartDate(batch.getStartDate());
				// b.setEndDate();
				b.setTimings(batch.getTimings());
				b.setDuration(batch.getDuration());
				b.setFee(batch.getFee());
				batchrepo.save(b);
			} else
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Batch id Not Found!");
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
		} catch (Exception ex) {
			throw ex;
		}
	}

	// 6
	// <---------------Running batches---------------------->
	@GetMapping("/runningbatches")
	@Operation(summary = "currently running batches", description = "List of batches that are currently running")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List of Running batches"),
			@ApiResponse(responseCode = "204", description = "No running batches"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Batch> getPresentRunningBatches() {
		try {
			List<Batch> b = batchrepo.getRunningBatch(LocalDate.now());
			return b;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
		} catch (Exception ex) {
			throw ex;
		}
	}

	// 7
	// <---------------Completed batches---------------------->
	@GetMapping("/completedbatches")
	@Operation(summary = "completed batches", description = "List of batches that are completed")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List of completed batches"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Batch> getcompletedBatches() {
		try {
			List<Batch> b = batchrepo.getCompletedBatch(LocalDate.now());
			return b;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
		} catch (Exception ex) {
			throw ex;
		}
	}

	// 11
	// <--------------- batches by the given code---------------------->
	@GetMapping("/batchesbycode/{code}")
	@Operation(summary = "batches by course code", description = "List of batches of a particular course")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List of batches for the specified course"),
			@ApiResponse(responseCode = "404", description = "No batches found for the specified course"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Batch> getBatchesByCode(@PathVariable("code") String code) {
		try {
			List<Batch> b = batchrepo.findByCode(code);
			return b;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
		} catch (Exception ex) {
			throw ex;
		}
	}

	// 13
	// <--------running batches between given two dates------------->
	@GetMapping("/runningbatchesbydates")
	@Operation(summary = "batches between two dates", description = "List for batches started between given two dates")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List of batches with start dates within the specified date range"),
			@ApiResponse(responseCode = "404", description = "No batches found within the specified date range"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Batch> getBatchesbetweentwodates(@RequestParam("firstdate") LocalDate firstdate,
			@RequestParam("seconddate") LocalDate seconddate) {
		try {
			List<Batch> b = batchrepo.getRunningBatchBetween(firstdate, seconddate);
			return b;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
		} catch (Exception ex) {
			throw ex;
		}
	}

}
