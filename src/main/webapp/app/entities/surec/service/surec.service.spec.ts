import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISurec, Surec } from '../surec.model';

import { SurecService } from './surec.service';

describe('Surec Service', () => {
  let service: SurecService;
  let httpMock: HttpTestingController;
  let elemDefault: ISurec;
  let expectedResult: ISurec | ISurec[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SurecService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      adi: 'AAAAAAA',
      aciklama: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Surec', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Surec()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Surec', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          adi: 'BBBBBB',
          aciklama: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Surec', () => {
      const patchObject = Object.assign(
        {
          adi: 'BBBBBB',
        },
        new Surec()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Surec', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          adi: 'BBBBBB',
          aciklama: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Surec', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSurecToCollectionIfMissing', () => {
      it('should add a Surec to an empty array', () => {
        const surec: ISurec = { id: 123 };
        expectedResult = service.addSurecToCollectionIfMissing([], surec);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(surec);
      });

      it('should not add a Surec to an array that contains it', () => {
        const surec: ISurec = { id: 123 };
        const surecCollection: ISurec[] = [
          {
            ...surec,
          },
          { id: 456 },
        ];
        expectedResult = service.addSurecToCollectionIfMissing(surecCollection, surec);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Surec to an array that doesn't contain it", () => {
        const surec: ISurec = { id: 123 };
        const surecCollection: ISurec[] = [{ id: 456 }];
        expectedResult = service.addSurecToCollectionIfMissing(surecCollection, surec);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(surec);
      });

      it('should add only unique Surec to an array', () => {
        const surecArray: ISurec[] = [{ id: 123 }, { id: 456 }, { id: 63586 }];
        const surecCollection: ISurec[] = [{ id: 123 }];
        expectedResult = service.addSurecToCollectionIfMissing(surecCollection, ...surecArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const surec: ISurec = { id: 123 };
        const surec2: ISurec = { id: 456 };
        expectedResult = service.addSurecToCollectionIfMissing([], surec, surec2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(surec);
        expect(expectedResult).toContain(surec2);
      });

      it('should accept null and undefined values', () => {
        const surec: ISurec = { id: 123 };
        expectedResult = service.addSurecToCollectionIfMissing([], null, surec, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(surec);
      });

      it('should return initial array if no Surec is added', () => {
        const surecCollection: ISurec[] = [{ id: 123 }];
        expectedResult = service.addSurecToCollectionIfMissing(surecCollection, undefined, null);
        expect(expectedResult).toEqual(surecCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
